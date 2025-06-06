package com.yenthefromghent.sls.core.lsp;

import com.yenthefromghent.sls.core.thread.ProccesManager;

public class Lsp implements Runnable {

    private final ProccesManager manager;

    private boolean done = false;

    public Lsp() {
        this.manager = new ProccesManager();
        RpcRequestParser rpcRequestParser = new RpcRequestParser();
        new Thread(rpcRequestParser).start();
    }


    /* main loop, we take a request from the from the registery, and handle it, until we get the exit notification */
    @Override
    public void run() {
        while (!done) {
            manager.handleNextRequest();
        }
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
