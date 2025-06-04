package com.yenthefromghent.sjls;

import com.yenthefromghent.sjls.core.logging.LoggingSetup;
import com.yenthefromghent.sjls.core.lsp.LSP;

import java.util.logging.Level;

public class Launch {

    public static void main(String[] args) {
        //initialize loggin
        LoggingSetup.setupLogging("/tmp/jsls_log.xml", Level.FINEST);

        //start reading from stdin
        try {
            LSP.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
