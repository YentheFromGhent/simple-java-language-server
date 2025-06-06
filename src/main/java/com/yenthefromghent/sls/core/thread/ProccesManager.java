package com.yenthefromghent.sls.core.thread;

import com.yenthefromghent.sls.core.lsp.RpcRequestStorer;
import com.yenthefromghent.sls.core.lsp.RpcRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

/** class that wait's for a request to come in, and then delegates it **/
public class ProccesManager {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcRequestStorer rpcRequestStorer;

    public ProccesManager() {
        rpcRequestStorer = new RpcRequestStorer();
    }

    public RpcRequest getNextRequest() {
        RpcRequest request = null;
        try {
            request = rpcRequestStorer.get();
        } catch (Exception e) {
            //if this fails, we will try again later.
            LOGGER.log(Level.SEVERE, "Exception in handleNextRequest", e);
            //TODO Make this try again later.
        }
        return request;
    }

    public void handleNextRequest() {
        RpcRequest request = getNextRequest();
        if (request == null) {
            //TODO check state of system.
        }

    }

}
