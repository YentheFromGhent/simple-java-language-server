package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.lsp.methods.Exit;
import com.yenthefromghent.sjls.core.lsp.methods.Initialize;
import com.yenthefromghent.sjls.core.lsp.methods.Initialized;
import com.yenthefromghent.sjls.core.lsp.methods.RpcMethod;
import com.yenthefromghent.sjls.core.lsp.methods.Shutdown;
import com.yenthefromghent.sjls.core.state.StatesRegistery;

import java.util.HashMap;
import java.util.Map;

public class RpcMethodRegistery {

    private final Map<String, RpcMethod> rpcMethods = new HashMap<>();

    //All the methods we support in the lsp go here for now.
    public RpcMethodRegistery(StatesRegistery statesRegistery) {
        rpcMethods.put("initialize", new Initialize());
        rpcMethods.put("intialized", new Initialized());
        rpcMethods.put("exit", new Exit());
        rpcMethods.put("shutdown", new Shutdown(statesRegistery));
    }

    public RpcMethod getMethod(String methodName) {
        if (rpcMethods.containsKey(methodName)) {
            return rpcMethods.get(methodName);
        }
        return null;
    }

}
