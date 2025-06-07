package com.yenthefromghent.sjls.core.lsp.methods;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.lsp.RpcResponseManager;
import com.yenthefromghent.sjls.core.state.ShutDownReceivedState;
import com.yenthefromghent.sjls.core.state.StatesRegistery;

import java.util.logging.Logger;

public class Shutdown implements RpcMethod, Request {

    private final StatesRegistery statesRegistery;

    public Shutdown(StatesRegistery statesRegistery) {
        this.statesRegistery = statesRegistery;
    }

    @Override
    public void invoke(JsonObject request) {
        // We add this state, so on exit we now that we received this request first
        // if we did not receive this request first, we exit with code 1, implying that
        // something went wrong
        statesRegistery.add(new ShutDownReceivedState());
        int id = request.get("id").getAsInt();
        succes(id);
    }

    @Override
    public void succes(int id) {
        RpcResponseManager.sendResponse(id);
    }

    @Override
    public void fail(int id) {
        //TODO reply with error
    }

}
