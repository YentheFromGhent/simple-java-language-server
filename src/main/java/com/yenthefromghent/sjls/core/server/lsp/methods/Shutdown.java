package com.yenthefromghent.sjls.core.server.lsp.methods;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.state.ShutDownReceivedState;
import com.yenthefromghent.sjls.core.state.StatesRegistery;

public class Shutdown implements RpcMethod {

    private final StatesRegistery statesRegistery;

    public Shutdown(StatesRegistery statesRegistery) {
        this.statesRegistery = statesRegistery;
    }

    // This is a special case which i have explained in the RpcAsyncRequestParser, but i give a quick recap.
    // We have already send this reply, because it is extremely time sensitive, so here we just add the State, which is
    // Usefull for the server itself, this has nothing to do with the client.
    @Override
    public void invoke(JsonObject request) {
        statesRegistery.add(new ShutDownReceivedState());
    }

}
