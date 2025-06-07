package com.yenthefromghent.sjls.core.lsp.async;

import com.yenthefromghent.sjls.core.lsp.RpcRequest;
import com.yenthefromghent.sjls.core.lsp.RpcRequestStorer;
import com.yenthefromghent.sjls.core.lsp.RpcResponseManager;
import com.yenthefromghent.sjls.core.util.AttributeExtracter;
import com.yenthefromghent.sjls.core.util.MessageCodec;
import com.yenthefromghent.sjls.core.util.RPCMessageCodec;
import com.yenthefromghent.sjls.core.io.RpcRequestReader;
import com.yenthefromghent.sjls.core.io.StdInRpcRequestReader;
import com.yenthefromghent.sjls.core.util.RpcAttributeExtracter;
import com.yenthefromghent.sjls.extra.Error;
import com.yenthefromghent.sjls.extra.exception.BlockedQueueOfferTimeOut;

import java.io.IOException;
import java.util.logging.Logger;

/** class that will take incoming messages from the StdinRpcRequestReader
  * and try to add them to the RpcRequestRegistery
  **/
public class RpcAsyncRequestParser implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("reader");

    private final RpcRequestReader rpcRequestReader;
    private final RpcRequestStorer rpcRequestStorer;
    private final MessageCodec messageCodec;

    public RpcAsyncRequestParser(RpcRequestStorer rpcRequestStorer) {
        LOGGER.finest("initializing RpcAsyncRequestParser");

        this.rpcRequestReader = new StdInRpcRequestReader();
        this.rpcRequestStorer = rpcRequestStorer;
        this.messageCodec = new RPCMessageCodec();
    }

    @Override
    public void run() {
        byte[] messageBytes;
        try {
            while ((messageBytes = rpcRequestReader.nextMessage()) != null) {
                LOGGER.finest("Next message received with content: " + new String(messageBytes));
                this.add(messageBytes, 0);
            }
        } catch (IOException e ) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private void add(byte[] messageBytes, int attempts) {
        try {
            RpcRequest something = new RpcRequest(messageCodec.decode(messageBytes));
            // This is not final and i will change this to be better later. The thing is that some clients
            // have extremely strict timeout limits for the shutdown request. to fix this, we should check whether a
            // request is the shutdown request at the beginning, which will immediately reply with the shutdown response
            // whilst bypassing all the rest, to have a average reponse time to this request of about 1ms.
            // The rest of the program can continue running and finishing its business synchronious after that, until we
            // eventually handle the exit request which closes the server gracefully.
            String method = something.request().get("method").getAsString();
            if (method != null && method.startsWith("shutdown")) {
                LOGGER.info("shutdown request quick send");
                int id = something.request().get("id").getAsInt();
                RpcResponseManager.sendResponse(id);
            }

            rpcRequestStorer.add(something);
        } catch (BlockedQueueOfferTimeOut e) {
            if (attempts >= 5) {
                //If we could not add the request to the queue after 5 times, the server should quit.
                LOGGER.severe(
                        "Could not add message to queue after " + attempts
                                + " attempts with error: " + e.getMessage()
                );
                throw new RuntimeException("attempts exceeded");
            }
            //could not add request to queue, tyring it again.
            LOGGER.warning("Could not add request to queue, will try again..");
            this.add(messageBytes, attempts + 1);
        } catch (RuntimeException e) {
            // We should not crash on a invalid rpc request, or when we exceed the attempts,
            // We should tell the client that we failed to parse this request and just move on.
            LOGGER.warning("invalid rpc request: " + e.getMessage());
            RpcResponseManager.sendErrorResponse(Error.PARSE_ERROR.error(), "could not parse request");
        }
    }

}
