package com.yenthefromghent.sjls.core.server.lsp.methods;

import com.google.gson.JsonObject;
import java.util.logging.Logger;

public class Initialized implements RpcMethod {

    private final Logger LOGGER = Logger.getLogger("main");

    @Override
    public void invoke(JsonObject request) {
        LOGGER.info("Initialized");
    }

}
