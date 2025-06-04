package com.yenthefromghent.sjls.core.lsp.methods;

import com.fasterxml.jackson.databind.JsonNode;
import com.yenthefromghent.sjls.core.lsp.RPCClass;
import com.yenthefromghent.sjls.core.lsp.RPCMethod;
import com.yenthefromghent.sjls.core.lsp.RPCMethodRegistery;
import com.yenthefromghent.sjls.core.lsp.RPCRequestHandler;


public class Initialized extends RPCClass implements Registerable {

    @RPCMethod
    public void initialized(JsonNode json) {
        LOGGER.finest("intialized method called");
        LOGGER.info("Initialized");
        RPCRequestHandler.setIsInitialized(true);
    }

    @Override
    public void register() throws NoSuchMethodException {
        RPCMethodRegistery.registerMethod(this.getClass().getMethod("initialized", JsonNode.class), this);
    }

}
