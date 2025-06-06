package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.codec.RpcAttributeExtracter;

public class RpcMethodInvoker {

    private static final RpcAttributeExtracter rpcAttributeExtracter = new RpcAttributeExtracter();

    private final RpcMethodRegistery methodRegistery = new RpcMethodRegistery();

    public void invokeMethod(RpcRequest rpcRequest) {
        String method = rpcAttributeExtracter.extractAttributeAsString(rpcRequest.request(), "method");

        if (method == null) {
            return;
        }


    }
}
