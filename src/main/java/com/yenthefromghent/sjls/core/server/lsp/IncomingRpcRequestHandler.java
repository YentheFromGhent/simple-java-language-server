package com.yenthefromghent.sjls.core.server.lsp;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.io.RpcResponseMessageWriter;
import com.yenthefromghent.sjls.core.util.MessageCodec;
import com.yenthefromghent.sjls.core.util.RPCMessageCodec;
import com.yenthefromghent.sjls.core.io.RequestReader;
import com.yenthefromghent.sjls.core.io.StdInRpcMessasgeReader;
import com.yenthefromghent.sjls.extra.exception.Error;
import com.yenthefromghent.sjls.extra.exception.BlockedQueueOfferTimeOut;

import java.io.IOException;
import java.util.logging.Logger;

/** class that will take incoming messages from the StdinRpcRequestReader
  * and try to add them to the RpcRequestRegistery
  **/
public class IncomingRpcRequestHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("reader");

    private final RequestReader rpcRequestReader;
    private final RpcMessageStorer rpcMessageStorer;
    private final MessageCodec messageCodec;

    public IncomingRpcRequestHandler(RpcMessageStorer rpcMessageStorer) {
        LOGGER.finest("initializing RpcAsyncRequestParser");

        this.rpcRequestReader = new StdInRpcMessasgeReader();
        this.rpcMessageStorer = rpcMessageStorer;
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

    // The thing is that some client have extremely strict timeout limits for the shutdown content.
    // to fix this, we should check whether a content is the shutdown content at the beginning,
    // which will immediately reply with the shutdown response whilst bypassing all the rest,
    // to have a average reponse time to this content of about 1ms.
    // The rest of the program can continue running and finishing its business synchronious after that, until we
    // eventually handle the exit content which closes the server gracefully.
    private void add(byte[] messageBytes, int attempts) {
        try {
            // Turn the message into a JsonObject, and check the type.
            JsonObject content = messageCodec.decode(messageBytes);
            RpcMessage message = new RpcMessage(content, messageTypeIdentifier(content));

            // Shutdown request shortcircuit, this will do for now, but should change later
            if (message.type() == RequestType.REQUEST) {
                String method = content.get("method").getAsString();
                if (method.startsWith("shutdown")) {
                    int id = content.get("id").getAsInt();
                    LOGGER.info("shutdown content quick send");
                    RpcResponseMessageWriter.sendResponse(id);
                }
            }

            rpcMessageStorer.add(message);
        } catch (BlockedQueueOfferTimeOut e) {
            //If we could not add the content to the queue after 5 times, the server should quit.
            if (attempts >= 5) {
                LOGGER.severe(
                        "Could not add message to queue after " + attempts
                                + " attempts with error: " + e.getMessage()
                );
                throw new RuntimeException("attempts exceeded");
            }
            LOGGER.warning("Could not add content to queue, will try again..");
            this.add(messageBytes, attempts + 1);
        } catch (RuntimeException e) {
            // We don't crash when we were unable to do anything with a message, but we do let the client now.
            LOGGER.warning("invalid rpc content: " + e.getMessage());
            RpcResponseMessageWriter.sendErrorResponse(Error.PARSE_ERROR.error(), "could not parse content");
        }
    }

    // Helper method to identify the type of message we received
    private RequestType messageTypeIdentifier(JsonObject request) {
        String id = request.get("id").getAsString();
        String method = request.get("method").getAsString();

        if (method == null) {
            return RequestType.RESPONSE;
        }

        if (id == null) {
            if (method.startsWith("$/")) {
                return RequestType.OPTIONAL_NOTIFICATION;
            } else {
                return RequestType.NOTIFICATION;
            }
        }

        return RequestType.REQUEST;
    }

}
