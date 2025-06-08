package com.yenthefromghent.sjls.core.server.lsp;

import com.yenthefromghent.sjls.core.server.Server;
import com.yenthefromghent.sjls.core.state.ShutdownState;
import com.yenthefromghent.sjls.core.state.StatesRegistery;

import java.util.logging.Logger;

public class RpcServerController implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcRequestDispatcher manager;
    private final Server server;

    public RpcServerController(StatesRegistery statesRegistery, Server server) {
        LOGGER.finest("initializing LSP");

        RpcMessageStorer requestStorer = new RpcMessageStorer();

        this.manager = new RpcRequestDispatcher(statesRegistery, requestStorer);
        this.server = server;

        statesRegistery.onState(ShutdownState.class, this);

        IncomingRpcRequestHandler incomingRpcRequestHandler = new IncomingRpcRequestHandler(requestStorer);
        new Thread(incomingRpcRequestHandler).start();
    }

    private boolean done = false;

    /* main loop, we take a content from the from the registery, and handle it, until we get the exit notification */
    public void loop() {
        while (!done) {
            LOGGER.finest("next content");
            manager.handleNextRequest();
        }

        server.shutdown();
    }

    public void run() {
       this.done = true;
       LOGGER.finest("shutting down LSP");
    }

}
