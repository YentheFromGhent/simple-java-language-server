package com.yenthefromghent.sjls.core.server.lsp.methods;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.io.RpcResponseMessageWriter;
import com.yenthefromghent.sjls.core.server.lsp.dto.Capabilities;
import com.yenthefromghent.sjls.core.server.lsp.dto.InitializeResult;
import com.yenthefromghent.sjls.core.server.lsp.dto.ServerInfo;

public class Initialize implements RpcMethod, Request {

    @Override
    public void invoke(JsonObject request) {
        int id = request.getAsJsonPrimitive("id").getAsInt();
        succes(id);
    }

    @Override
    public void succes(int id) {
       RpcResponseMessageWriter.sendResponse(new InitializeResult(new Capabilities(), new ServerInfo()), id);
    }

    @Override
    public void fail(int id ) {
        //TODO reply with error
    }

}