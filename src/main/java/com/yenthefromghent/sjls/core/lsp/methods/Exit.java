package com.yenthefromghent.sjls.core.lsp.methods;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.lsp.MethodRegistery;
import com.yenthefromghent.sjls.core.state.ShutdownState;
import com.yenthefromghent.sjls.core.state.StatesRegistery;

public class Exit implements RpcMethod {

    private final StatesRegistery statesRegistery;

    public Exit(StatesRegistery statesRegistery) {
        this.statesRegistery = statesRegistery;
    }

    @Override
    public void invoke(JsonObject request) {
        statesRegistery.add(new ShutdownState());
    }
}
