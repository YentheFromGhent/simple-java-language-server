package com.yenthefromghent.sjls.core.server.lsp.methods;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.server.lsp.RpcRequest;

public class RpcMessageDeserializer {

    //This method takes a RpcMessage and turn it into the base RpcRequest
    public RpcRequest deserialize(JsonObject json) {
        return new RpcRequest("yenthe", 1, null);
    }

}
