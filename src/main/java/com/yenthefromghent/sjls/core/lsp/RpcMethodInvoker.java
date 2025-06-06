package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.state.StatesRegistery;
import com.yenthefromghent.sjls.core.util.RpcAttributeExtracter;
import com.yenthefromghent.sjls.core.lsp.methods.RpcMethod;

public class RpcMethodInvoker {

    private static final RpcAttributeExtracter rpcAttributeExtracter = new RpcAttributeExtracter();

    private final RpcMethodRegistery methodRegistery;

    public RpcMethodInvoker(StatesRegistery statesRegistery) {
        this.methodRegistery = new RpcMethodRegistery(statesRegistery);
    }

    // extract the method from the rpc request and invoke it
    public void invokeMethod(RpcRequest rpcRequest) {
        String method = rpcAttributeExtracter.extractAttributeAsString(rpcRequest.request(), "method");

        if (method == null) {
            return;
        }

        RpcMethod methodClass = methodRegistery.getMethod(method);

        if (methodClass == null) {
            // this should not happen if we exchanged our capabilities correctly
            throw new RuntimeException("Method " + method + " not found");
        }

        methodClass.invoke(rpcRequest.request());
    }

}
