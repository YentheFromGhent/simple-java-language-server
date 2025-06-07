package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.server.Server;
import com.yenthefromghent.sjls.core.state.ShutdownState;
import com.yenthefromghent.sjls.core.state.StatesRegistery;

import java.util.logging.Logger;

public class Lsp implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcMethodHandler manager;
    private final Server server;
    private final Thread worker;

    public Lsp(StatesRegistery statesRegistery, Server server) {
        LOGGER.finest("initializing LSP");

        RpcRequestStorer requestStorer = new RpcRequestStorer();

        this.manager = new RpcMethodHandler(statesRegistery, requestStorer);
        this.server = server;

        //Register to this state, which will notify our loop(), to stop running;
        statesRegistery.onState(ShutdownState.class, this);

        RpcRequestParser rpcRequestParser = new RpcRequestParser(requestStorer);
        worker = new Thread(rpcRequestParser);
        worker.start();
    }

    private boolean done = false;

    /* main loop, we take a request from the from the registery, and handle it, until we get the exit notification */
    public void loop() {
        while (!done) {
            LOGGER.finest("next request");
            manager.handleNextRequest();
        }

        //We shutdown the server
        worker.interrupt();
        // server.shutdown();
    }

    @Override
    public void run() {
        LOGGER.finest("set done to true");
        done = true;
    }

}
