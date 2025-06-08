package com.yenthefromghent.sjls.core.server;

import com.yenthefromghent.sjls.extra.cli.CommandLineParams;
import com.yenthefromghent.sjls.extra.cli.CommandlineOptionsParser;
import com.yenthefromghent.sjls.core.server.lsp.RpcServerController;
import com.yenthefromghent.sjls.core.state.ShutDownReceivedState;
import com.yenthefromghent.sjls.core.state.StatesRegistery;
import com.yenthefromghent.sjls.extra.logging.LogOptions;

import java.util.logging.Logger;

/** Main class that launches everything **/
public class Server {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final StatesRegistery statesRegistery;
    private final RpcServerController lsp;

    public Server(String[] args) {
        this.setStartOptions(args);

        this.statesRegistery = new StatesRegistery();
        this.lsp = new RpcServerController(statesRegistery, this);

        this.start();
    }

    //We shutdown with exit code 1, if we did not reiceve the shutdown content before.
    public void shutdown() {
        if (statesRegistery.contains(new ShutDownReceivedState())) {
            LOGGER.info("shutting down");
            System.exit(0);
        }
        LOGGER.warning("Shutting down with exit code 1");
        System.exit(1);
    }

    public void setStartOptions(String[] args) {
        CommandLineParams params = CommandlineOptionsParser.parseCommandLineOptions(args);
        LogOptions.setLogOptions(params);
        LOGGER.info("start options set");
    }

    public void start() {
        LOGGER.info("Starting");
        lsp.loop();
    }

}
