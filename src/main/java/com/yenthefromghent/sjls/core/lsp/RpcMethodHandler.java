package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.state.StatesRegistery;
import com.yenthefromghent.sjls.core.util.JsonObjectCreater;
import com.yenthefromghent.sjls.core.util.RpcAttributeExtracter;

import java.util.logging.Level;
import java.util.logging.Logger;

/** class that wait's for a request to come in, and then delegates it **/
public class RpcMethodHandler {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcRequestStorer rpcRequestStorer;
    private final RpcMethodInvoker rpcMethodInvoker;

    public RpcMethodHandler(StatesRegistery statesRegistery) {
        rpcRequestStorer = new RpcRequestStorer();
        rpcMethodInvoker = new RpcMethodInvoker(statesRegistery);
    }

    public RpcRequest getNextRequest() {
        RpcRequest request = null;
        try {
            request = rpcRequestStorer.get();
        } catch (Exception e) {
            //if this fails, we will try again later.
            LOGGER.log(Level.SEVERE, "Could not get next item in MethodRegistery with error: ", e);
        }
        return request;
    }

    public void handleNextRequest() {
        RpcRequest request = getNextRequest();

        if (request == null) {
            //We did not get a request, try again later
            LOGGER.warning("Could not get request from queue, trying again");
            return;
        }

        // I hate switch
        switch (messageTypeIdentifier(request)) {
            case NOTIFICATION, REQUEST -> rpcMethodInvoker.invokeMethod(request);
        }

    }

    private final RpcAttributeExtracter attributeExtractor = new RpcAttributeExtracter();

    private MessageType messageTypeIdentifier(RpcRequest request) {
        String id = attributeExtractor.extractAttributeAsString(request.request(), "id");
        String method = attributeExtractor.extractAttributeAsString(request.request(), "method");

        if (id == null) {
            if (method.startsWith("$/")) {
                return MessageType.OPTIONAL_NOTIFICATION;
            } else {
                return MessageType.NOTIFICATION;
            }
        }

        if (method == null) {
            return MessageType.RESPONSE;
        }

        return MessageType.REQUEST;
    }

}
