package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.state.StatesRegistery;
import com.yenthefromghent.sjls.core.util.RpcAttributeExtracter;
import com.yenthefromghent.sjls.core.lsp.methods.RpcMethod;

import java.util.logging.Logger;

public class RpcMethodInvoker {

    private static final RpcAttributeExtracter rpcAttributeExtracter = new RpcAttributeExtracter();
    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcMethodRegistery methodRegistery;

    public RpcMethodInvoker(StatesRegistery statesRegistery) {
        LOGGER.finest("initializing RpcMethodInvoker");

        this.methodRegistery = new RpcMethodRegistery(statesRegistery);
    }

    // extract the method from the rpc request and invoke it
    public void invokeMethod(RpcRequest rpcRequest) {
        String method = rpcAttributeExtracter.extractAttributeAsString(rpcRequest.request(), "method");

        RpcMethod methodClass = methodRegistery.getMethod(method);

        if (methodClass == null) {
            // this should not happen if we exchanged our capabilities correctly
            // so we quit the system if it does
            LOGGER.severe("Method not found: " + method + ", exiting server");
            throw new RuntimeException("Method " + method + " not found");
        }

        LOGGER.finest("invoking method " + method + ", on methodclass " + methodClass);
        methodClass.invoke(rpcRequest.request());
    }

}
