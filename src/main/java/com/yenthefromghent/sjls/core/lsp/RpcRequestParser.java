package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.util.MessageCodec;
import com.yenthefromghent.sjls.core.util.RPCMessageCodec;
import com.yenthefromghent.sjls.core.io.RpcRequestReader;
import com.yenthefromghent.sjls.core.io.StdInRpcRequestReader;
import com.yenthefromghent.sjls.extra.Error;
import com.yenthefromghent.sjls.extra.exception.BlockedQueueOfferTimeOut;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/** class that will take incoming messages from the StdinRpcRequestReader
  * and try to add them to the RpcRequestRegistery
  **/
public class RpcRequestParser implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcRequestReader rpcRequestReader;
    private final RpcRequestStorer rpcRequestStorer;
    private final MessageCodec messageCodec;

    public RpcRequestParser() {
        this.rpcRequestReader = new StdInRpcRequestReader();
        this.rpcRequestStorer = new RpcRequestStorer();
        this.messageCodec = new RPCMessageCodec();
    }

    @Override
    public void run() {
        byte[] messageBytes;
        try {
            while ((messageBytes = rpcRequestReader.nextMessage()) != null) {
                this.add(messageBytes, 0);
            }
        } catch (IOException e ) {
            throw new RuntimeException(e.getMessage(), e);
        }

        //Add poison spill to the queue so that the server will shutdown.
        //This will also happen on the shutdown request.
        String poisonPillJson = """
                {
                  "jsonrpc": "2.0",
                  "method": "exit",
                  "params": {}
                }""";
        this.add(poisonPillJson.getBytes(StandardCharsets.UTF_8), 0);
    }

    private void add(byte[] messageBytes, int attempts) {
        try {
            rpcRequestStorer.add(new RpcRequest(messageCodec.decode(messageBytes)));
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
