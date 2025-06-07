package com.yenthefromghent.sjls.core.lsp.methods;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.lsp.RpcResponseManager;
import com.yenthefromghent.sjls.core.lsp.json_types.Capabilities;
import com.yenthefromghent.sjls.core.lsp.json_types.InitializeResult;
import com.yenthefromghent.sjls.core.lsp.json_types.ServerInfo;

public class Initialize implements RpcMethod, Request {

    @Override
    public void invoke(JsonObject request) {
        int id = request.getAsJsonPrimitive("id").getAsInt();
        succes(id);
    }

    @Override
    public void succes(int id) {
       RpcResponseManager.sendResponse(new InitializeResult(new Capabilities(), new ServerInfo()), id);
    }

    @Override
    public void fail(int id ) {
        //TODO reply with error
    }

}