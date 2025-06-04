package com.yenthefromghent.sjls.core.lsp.methods;

import com.yenthefromghent.sjls.core.lsp.RPCMethod;
import com.yenthefromghent.sjls.core.lsp.RPCMethodRegistery;
import com.yenthefromghent.sjls.core.lsp.RPCRequestHandler;
import com.yenthefromghent.sjls.core.lsp.types.*;

public class Initialize implements Registerable, RPCInstance {

    @RPCMethod
    public void initialize(Object[] params, RPCRequestHandler handler) {
        if (params.length != 0) {
            succes(handler);
        } else {
            error(handler);
        }
    }

    @Registered
    @Override
    public void register() throws NoSuchMethodException {
        RPCMethodRegistery.registerMethod(this.getClass().getMethod(this.getClass().getSimpleName()), this.getClass());
    }

    @Override
    public void succes(RPCRequestHandler handler) {
        InitializeResult succesResponse = new InitializeResult();
        succesResponse.capabilities = new Capabilities();
        succesResponse.serverInfo = new ServerInfo();
        handler.reply(succesResponse);
    }


    @Override
    public void error(RPCRequestHandler handler) {
        InitializeError errorReponse = new InitializeError();
        errorReponse.retry = false;
        handler.reply(errorReponse);
    }

}
