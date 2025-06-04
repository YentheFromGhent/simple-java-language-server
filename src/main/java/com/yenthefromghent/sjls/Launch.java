package com.yenthefromghent.sjls;

import com.yenthefromghent.sjls.core.logging.LoggingSetup;
import com.yenthefromghent.sjls.core.lsp.LSP;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Launch {

    private static final Logger LOGGER = Logger.getLogger("main");

    public static void main(String[] args) {
        //initialize loggin
        LoggingSetup.setupLogging("/tmp/jsls_log.xml", Level.FINEST);

        //start server
        try {
            LSP.start();
            LSP.run();
        } catch (Exception e) {
            LOGGER.severe("Could not start server with error: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
