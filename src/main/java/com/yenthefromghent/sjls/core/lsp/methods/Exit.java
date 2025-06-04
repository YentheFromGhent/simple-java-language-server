package com.yenthefromghent.sjls.core.lsp.methods;

import com.fasterxml.jackson.databind.JsonNode;
import com.yenthefromghent.sjls.core.lsp.LSP;
import com.yenthefromghent.sjls.core.lsp.RPCClass;
import com.yenthefromghent.sjls.core.lsp.RPCMethod;
import com.yenthefromghent.sjls.core.lsp.RPCMethodRegistery;

public class Exit extends RPCClass implements Registerable {

    @RPCMethod
    public void exit(JsonNode node) {
        LOGGER.finest("Exit method called");
        LSP.stop();
    }

    @Override
    public void register() throws NoSuchMethodException {
        RPCMethodRegistery.registerMethod(
                this.getClass().getMethod("exit", JsonNode.class), this
        );
    }
}
