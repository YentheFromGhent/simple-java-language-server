package com.yenthefromghent.sjls.core.lsp.methods;

import com.yenthefromghent.sjls.core.lsp.RPCMethodRegistery;
import com.yenthefromghent.sjls.core.lsp.RPCRequestHandler;

public interface RPCInstance {

    void succes(RPCRequestHandler handler);

    void error(RPCRequestHandler handler);

}
