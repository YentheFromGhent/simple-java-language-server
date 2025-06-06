package com.yenthefromghent.sls;

import com.yenthefromghent.sls.core.cli.CommandLineOptionsParser;
import com.yenthefromghent.sls.core.cli.CommandLineParams;
import com.yenthefromghent.sls.core.lsp.Lsp;
import com.yenthefromghent.sls.debug.LogOptions;

public class Main {

    public static final Lsp LSP = new Lsp();

    public static void main(String[] args) {
        start(args);
    }

    public static void start(String[] args) {
        CommandLineParams params = CommandLineOptionsParser.parseCommandLineOptions(args);
        LogOptions.setLogOptions(params);
        LSP.run();
    }

}
