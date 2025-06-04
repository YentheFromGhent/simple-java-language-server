package com.yenthefromghent.sjls;

import com.yenthefromghent.sjls.core.logging.LoggingSetup;

import java.util.logging.Level;

public class Launch {

    public static void main(String[] args) {
        //initialize loggin
        LoggingSetup.setupLogging("/tmp/jsls_log.txt", Level.FINEST);

        //start reading from stdin
        /* try {
           LSP.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

}
