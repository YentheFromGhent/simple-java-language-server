package com.yenthefromghent.sjls.core.logging;

import java.util.logging.*;

public class LoggingSetup {

    public static void setupLogging(String filepath, Level level) {
        try {
            //Remove the defualt logger that writes to std out/err
            Logger LOGGER = Logger.getLogger("main");
            for (Handler handler : LOGGER.getParent().getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    LOGGER.getParent().removeHandler(handler);
                }
            }

            //Add loger to write to filepath
            FileHandler fileHandler = new FileHandler(filepath);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(level);
            LOGGER.log(Level.INFO, "Logger intialized");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
