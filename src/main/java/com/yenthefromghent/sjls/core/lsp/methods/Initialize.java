package com.yenthefromghent.sjls.core.lsp.methods;

import com.yenthefromghent.sjls.core.lsp.MethodHandler;
import com.yenthefromghent.sjls.core.lsp.RPCMethod;

public class Initialize implements Registerable {


    @RPCMethod
    @Override
    public void register() {
        MethodHandler.registerMethod(this.getClass().getEnclosingMethod(), this.getClass());
    }

}
