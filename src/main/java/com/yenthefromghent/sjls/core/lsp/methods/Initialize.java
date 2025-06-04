package com.yenthefromghent.sjls.core.lsp.methods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yenthefromghent.sjls.core.lsp.*;
import com.yenthefromghent.sjls.core.lsp.error.ErrorCode;
import com.yenthefromghent.sjls.core.lsp.error.ResponseError;
import com.yenthefromghent.sjls.core.lsp.message.ResponseMessage;
import com.yenthefromghent.sjls.core.lsp.types.*;

public class Initialize extends RPCClass implements RPCInstance, Registerable {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @RPCMethod
    public void initialize(JsonNode message) {
        LOGGER.finest("intialize method called");
        int id = message.get("id").asInt();

        LOGGER.finest("Trying to parse parameters from Initialize into InitialiazeParams");
        InitializeParams params = deserialize(message.get("params"), InitializeParams.class);

        if (params != null) {
            succes(id);
        } else {
            error(id);
        }
    }

    @Override
    public void register() throws NoSuchMethodException {
        RPCMethodRegistery.registerMethod(
                this.getClass().getMethod("initialize", JsonNode.class), this
        );
    }

    @Override
    public void succes(int id) {
        LOGGER.finest("Invoking initialize succes");
        LSP.send(
                new ResponseMessage(id, new InitializeResult(new Capabilities(), new ServerInfo()))
        );
    }


    @Override
    public void error(int id) {
        LOGGER.warning("Error parameters were null");
        LSP.send(new ResponseMessage(id, new ResponseError(
                ErrorCode.UNKNOWERROR, "could not invoke intialize", new InitializeError(false)))
        );
    }

}
