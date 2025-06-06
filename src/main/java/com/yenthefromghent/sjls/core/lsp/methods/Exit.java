package com.yenthefromghent.sjls.core.lsp.methods;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.lsp.Lsp;

public class Exit implements RpcMethod {

    @Override
    public void invoke(JsonObject request) {
        //This will make the server shutdown.
        Lsp.setDone(true);
    }
}
