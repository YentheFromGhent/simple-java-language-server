package com.yenthefromghent.sjls.core.lsp.methods;

import com.yenthefromghent.sjls.core.lsp.RPCMethodRegistery;
import com.yenthefromghent.sjls.core.lsp.RPCRequestHandler;

public interface RPCInstance {

    void succes(int id);

    void error(int id);

}
