package com.yenthefromghent.sjls.core.lsp.methods;

import com.fasterxml.jackson.databind.JsonNode;
import com.yenthefromghent.sjls.core.lsp.*;
import com.yenthefromghent.sjls.core.lsp.error.ErrorCode;
import com.yenthefromghent.sjls.core.lsp.error.ResponseError;
import com.yenthefromghent.sjls.core.lsp.message.ResponseMessage;

public class Shutdown extends RPCClass implements Registerable, RPCInstance {

    @RPCMethod
    public void shutdown(JsonNode message) {
        LOGGER.finest("shutdown method called");
        int id = message.get("id").asInt();

        if (RPCRequestHandler.isReceivedShutdownRequest()) {
            error(id);
        }

        RPCRequestHandler.setReceivedShutdownRequest(true);
        succes(id);
    }

    @Override
    public void register() throws NoSuchMethodException {
        RPCMethodRegistery.registerMethod(
                this.getClass().getMethod("shutdown", JsonNode.class), this
        );
    }

    @Override
    public void succes(int id) {
        LSP.send(new ResponseMessage(id));
    }

    @Override
    public void error(int id) {
        LSP.send(new ResponseMessage(
                id, new ResponseError(ErrorCode.SHUTDOWNALREADYRECEIVED, "Shutdown request already received"))
        );
    }

}
