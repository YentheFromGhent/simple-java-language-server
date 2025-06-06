package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.server.ServerShutdown;
import com.yenthefromghent.sjls.core.state.StatesRegistery;

public class Lsp implements Runnable {

    private final RpcMethodHandler manager;
    private final ServerShutdown serverShutdown;

    public Lsp() {
        StatesRegistery statesRegistery = new StatesRegistery();

        this.manager = new RpcMethodHandler(statesRegistery);
        this.serverShutdown = new ServerShutdown(statesRegistery);

        RpcRequestParser rpcRequestParser = new RpcRequestParser();
        new Thread(rpcRequestParser).start();
    }

    private static boolean done = false;

    /* main loop, we take a request from the from the registery, and handle it, until we get the exit notification */
    @Override
    public void run() {
        while (!done) {
            manager.handleNextRequest();
        }

        //We shutdown the server
        serverShutdown.shutdown();
    }

    //I hate how i do this. should fix
    public static void setDone(boolean done) {
        Lsp.done = done;
    }

}
