package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.lsp.methods.Initialize;
import com.yenthefromghent.sjls.core.lsp.methods.Initialized;
import com.yenthefromghent.sjls.core.lsp.methods.RpcMethod;

import java.util.HashMap;
import java.util.Map;

public class RpcMethodRegistery {

    private static final Map<String, RpcMethod> rpcMethods = new HashMap<>();

    //All the methods we support in the lsp go here for now.
    static {
        rpcMethods.put("initialize", Initialize::new);
        rpcMethods.put("intialized", Initialized::new);
    }

    public RpcMethod getMethod(String methodName) {
        if (rpcMethods.containsKey(methodName)) {
            return rpcMethods.get(methodName);
        }
        return null;
    }

}
