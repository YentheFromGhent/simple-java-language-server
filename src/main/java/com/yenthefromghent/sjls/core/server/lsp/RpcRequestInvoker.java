package com.yenthefromghent.sjls.core.server.lsp;

import com.yenthefromghent.sjls.core.server.lsp.methods.RpcMethodRegistery;
import com.yenthefromghent.sjls.core.state.StatesRegistery;
import com.yenthefromghent.sjls.core.server.lsp.methods.RpcMethod;

import java.util.logging.Logger;

public class RpcRequestInvoker {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcMethodRegistery methodRegistery;

    public RpcRequestInvoker(StatesRegistery statesRegistery) {
        LOGGER.finest("initializing RpcMethodInvoker");

        this.methodRegistery = new RpcMethodRegistery(statesRegistery);
    }

    // extract the method from the rpc content and invoke it
    public void invokeMethod(RpcMessage rpcMessage) {
        String method = rpcMessage.content().get("method").getAsString();

        RpcMethod methodClass = methodRegistery.getMethod(method);

        if (methodClass == null) {
            // this should not happen if we exchanged our capabilities correctly
            // so we quit the system if it does
            LOGGER.severe("Method not found: " + method + ", exiting server");
            throw new RuntimeException("Method " + method + " not found");
        }

        LOGGER.finest("invoking method " + method + ", on methodclass " + methodClass);
        methodClass.invoke(rpcMessage.content());
    }

}
