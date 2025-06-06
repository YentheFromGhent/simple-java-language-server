package com.yenthefromghent.sjls.core.cli;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** simple class that parses the command line options **/
public class CommandLineOptionsParser {

    private static final Logger LOGGER = Logger.getLogger("main");

    public static CommandLineParams parseCommandLineOptions(String[] args) {
        Level level = Level.FINEST;
        String filepath = "log.xml";

        Map<String, Level> levels = new HashMap<>();

        //This map i used to get the right level object after parsing
        levels.put("FINEST", Level.FINEST);
        levels.put("FINER", Level.FINER);
        levels.put("FINE", Level.FINE);
        levels.put("INFO", Level.INFO);
        levels.put("WARN", Level.WARNING);
        levels.put("ERROR", Level.SEVERE);
        levels.put("DEBUG", Level.ALL);


        Options options = new Options();

        Option logLevel = new Option("l", "level", true, "log level");
        options.addOption(logLevel);

        Option filePathLocation = new Option("f", "filepath", true, "file path");
        options.addOption(filePathLocation);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String logType = cmd.getOptionValue("level");
            if (logType != null) {
                if (!levels.containsKey(logType.toUpperCase())) {
                    throw new IllegalArgumentException("Unknown log level: " + logType);
                }
                level = levels.get(logType.toUpperCase());
            }
            String path = cmd.getOptionValue("filepath");
            if (path != null) {
                filepath = path;
            }
        } catch (ParseException e) {
            //we just write to stdout, because the server never started.
            System.err.println("Error parsing command line options: " + e.getMessage());
        }

        return new CommandLineParams(level, filepath);
    }
}
