package com.yenthefromghent.sjls.core.server;

import com.yenthefromghent.sjls.core.state.ShutdownReceived;
import com.yenthefromghent.sjls.core.state.StatesRegistery;
import java.util.logging.Logger;

public class ServerShutdown {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final StatesRegistery statesRegistery;

    public ServerShutdown(StatesRegistery statesRegistery) {
        this.statesRegistery = statesRegistery;
    }

    //We shutdown with exit code 1, if we did not reiceve the shutdown request before.
    public void shutdown() {
        if (statesRegistery.contains(new ShutdownReceived())) {
            LOGGER.info("Server is shutting down");
            System.exit(0);
        }
        LOGGER.warning("Shutting down with exit code 1");
        System.exit(1);
    }

}
