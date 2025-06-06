package com.yenthefromghent.sls.debug;

import com.yenthefromghent.sls.core.cli.CommandLineParams;

import java.util.logging.*;

public class LogOptions {

    public static void setLogOptions(CommandLineParams params) {
        try {
            //Remove the defualt logger that writes to std out/err
            Logger LOGGER = Logger.getLogger("main");
            for (Handler handler : LOGGER.getParent().getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    LOGGER.getParent().removeHandler(handler);
                }
            }

            //Add loger to write to filepath
            FileHandler fileHandler = new FileHandler(params.filepath());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(params.level());

            LOGGER.log(Level.INFO, "Logger intialized");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
