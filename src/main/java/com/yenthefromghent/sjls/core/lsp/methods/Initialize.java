package com.yenthefromghent.sjls.core.lsp.methods;

import com.yenthefromghent.sjls.core.lsp.LSP;
import com.yenthefromghent.sjls.core.lsp.RPCMethod;
import com.yenthefromghent.sjls.core.lsp.RPCMethodRegistery;
import com.yenthefromghent.sjls.core.lsp.error.ErrorCode;
import com.yenthefromghent.sjls.core.lsp.error.ResponseError;
import com.yenthefromghent.sjls.core.lsp.message.ResponseMessage;
import com.yenthefromghent.sjls.core.lsp.types.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Initialize implements RPCInstance, Registerable {

    private final Logger LOGGER = Logger.getLogger("main");

    @RPCMethod
    public void initialize(Object[] params, int id) {
        LOGGER.log(Level.INFO, "Invoking initialize from Initialize");
        if (params.length > 0) {
            succes(id);
        } else {
            error(id);
        }
    }

    @Override
    public void register() throws NoSuchMethodException {
        RPCMethodRegistery.registerMethod(
                this.getClass().getMethod("initialize", Object[].class, int.class), this
        );
    }

    @Override
    public void succes(int id) {
        LOGGER.finest("Invoking succes");
        LSP.send(
                new ResponseMessage(id, new InitializeResult(new Capabilities(), new ServerInfo()))
        );
    }


    @Override
    public void error(int id) {
        LOGGER.warning("Invoking error");
        LSP.send(new ResponseMessage(id, new ResponseError(
                ErrorCode.UNKNOWERROR, "could not invoke intialize", new InitializeError(false)))
        );
    }

}
