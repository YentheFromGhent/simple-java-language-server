package com.yenthefromghent.sjls.core.lsp.methods;

import com.yenthefromghent.sjls.core.lsp.RPCMethod;
import com.yenthefromghent.sjls.core.lsp.RPCMethodRegistery;
import com.yenthefromghent.sjls.core.lsp.RPCRequestHandler;

import java.util.logging.Logger;

public class Initialized implements Registerable {

    private static final Logger LOGGER = Logger.getLogger(Initialized.class.getName());

    @RPCMethod
    public void intialized() {
        LOGGER.info("Initialized");
        RPCRequestHandler.setIsInitialized(true);
    }

    @Override
    public void register() throws NoSuchMethodException {
        RPCMethodRegistery.registerMethod(this.getClass().getMethod("intialized"), this);
    }

}
