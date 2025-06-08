package com.yenthefromghent.sjls.core.server.lsp.methods;

import com.yenthefromghent.sjls.core.state.StatesRegistery;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RpcMethodRegistery {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final Map<String, RpcMethod> rpcMethods = new HashMap<>();

    //All the methods we support in the lsp go in this list
    public RpcMethodRegistery(StatesRegistery statesRegistery) {
        LOGGER.finest("Initializing RpcMethodRegistery");

        rpcMethods.put("initialize", new Initialize());
        rpcMethods.put("initialized", new Initialized());
        rpcMethods.put("exit", new Exit(statesRegistery));
        rpcMethods.put("shutdown", new Shutdown(statesRegistery));
    }

    public RpcMethod getMethod(String methodName) {
        if (rpcMethods.containsKey(methodName)) {
            return rpcMethods.get(methodName);
        }
        return null;
    }

}
