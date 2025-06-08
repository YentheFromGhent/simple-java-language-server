package com.yenthefromghent.sjls.extra.logging;

import com.yenthefromghent.sjls.extra.cli.CommandLineParams;

import java.util.logging.*;

/** sets the options for logging in the server **/
public class LogOptions {

    public static void setLogOptions(CommandLineParams params) {
        try {
            //Remove the defualt logger that writes to std out/err
            Logger LOGGER = Logger.getLogger("main");
            Logger READER_LOGGER = Logger.getLogger("reader");
            for (Handler handler : LOGGER.getParent().getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    LOGGER.getParent().removeHandler(handler);
                }
            }

            for (Handler handler : READER_LOGGER.getParent().getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    LOGGER.getParent().removeHandler(handler);
                }
            }

            // Set LOGGER to write to filepath
            FileHandler fileHandler = new FileHandler(params.filepath());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(params.level());

            // Let READER_LOGGER write to somewhere
            FileHandler fileHandler2 = new FileHandler("/home/yenthe/Runnable/reader_log.xml");
            READER_LOGGER.addHandler(fileHandler2);
            READER_LOGGER.setLevel(Level.ALL);

            LOGGER.log(Level.INFO, "Logger intialized");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
