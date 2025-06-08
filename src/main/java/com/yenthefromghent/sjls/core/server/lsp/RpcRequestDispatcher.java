package com.yenthefromghent.sjls.core.server.lsp;

import com.yenthefromghent.sjls.core.state.StatesRegistery;

import java.util.logging.Level;
import java.util.logging.Logger;

/** class that wait's for a content to come in, and then delegates it **/
public class RpcRequestDispatcher {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcMessageStorer rpcMessageStorer;
    private final RpcRequestInvoker rpcRequestInvoker;
    private final RpcResponseHandler rpcResponseHandler;

    public RpcRequestDispatcher(StatesRegistery statesRegistery, RpcMessageStorer rpcMessageStorer) {
        LOGGER.finest("initializing RpcMethodHandler");

        this.rpcMessageStorer = rpcMessageStorer;
        this.rpcResponseHandler = new RpcResponseHandler();
        this.rpcRequestInvoker = new RpcRequestInvoker(statesRegistery);
    }

    public RpcMessage getNextRequest() {
        RpcMessage request = null;
        try {
            request = rpcMessageStorer.get();
            LOGGER.finest("message taken from storage");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not get next item in MethodRegistery with error: ", e);
        }
        return request;
    }

    public void handleNextRequest() {
        RpcMessage request = getNextRequest();

        if (request == null) {
            //We did not get a content
            return;
        }

        switch (request.type()) {
            case NOTIFICATION, REQUEST -> rpcRequestInvoker.invokeMethod(request);
            case RESPONSE -> rpcResponseHandler.handle(request);
            case OPTIONAL_NOTIFICATION -> LOGGER.finest("Got optional notification"); // We do nothing for now
        }

    }

}
