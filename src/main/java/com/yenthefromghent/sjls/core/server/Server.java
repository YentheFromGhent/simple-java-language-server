package com.yenthefromghent.sjls.core.server;

import com.yenthefromghent.sjls.core.cli.CommandLineParams;
import com.yenthefromghent.sjls.core.cli.CommandlineOptionsParser;
import com.yenthefromghent.sjls.core.lsp.Lsp;
import com.yenthefromghent.sjls.core.state.ShutDownReceivedState;
import com.yenthefromghent.sjls.core.state.StatesRegistery;
import com.yenthefromghent.sjls.extra.LogOptions;

import java.util.logging.Logger;

public class Server {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final StatesRegistery statesRegistery;
    private final Lsp lsp;

    public Server(String[] args) {
        this.startOptions(args);

        this.statesRegistery = new StatesRegistery();
        this.lsp = new Lsp(statesRegistery, this);

        this.start();
    }

    //We shutdown with exit code 1, if we did not reiceve the shutdown request before.
    public void shutdown() {
        if (statesRegistery.contains(new ShutDownReceivedState())) {
            LOGGER.info("Server is shutting down");
            System.exit(0);
        }
        LOGGER.warning("Shutting down with exit code 1");
        System.exit(1);
    }


    public void startOptions(String[] args) {
        CommandLineParams params = CommandlineOptionsParser.parseCommandLineOptions(args);
        LogOptions.setLogOptions(params);
        LOGGER.info("start options set");
    }

    public void start() {
        LOGGER.info("Starting");
        lsp.loop();
    }

}
