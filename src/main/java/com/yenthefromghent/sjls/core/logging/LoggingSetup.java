package com.yenthefromghent.sjls.core.logging;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingSetup {

    public static void setupLogging(String filepath, Level level) {
        try {
            FileHandler fileHandler = new FileHandler(filepath);
            Logger LOGGER = Logger.getLogger("main");
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(level);
            LOGGER.log(Level.FINEST, "Logger intialized");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
