package com.yenthefromghent.sjls;

import com.yenthefromghent.sjls.core.cli.CommandlineOptionsParser;
import com.yenthefromghent.sjls.core.cli.CommandLineParams;
import com.yenthefromghent.sjls.core.lsp.Lsp;
import com.yenthefromghent.sjls.extra.LogOptions;

public class Main {

    public static final Lsp LSP = new Lsp();

    public static void main(String[] args) {
        start(args);
    }

    public static void start(String[] args) {
        CommandLineParams params = CommandlineOptionsParser.parseCommandLineOptions(args);
        LogOptions.setLogOptions(params);
        LSP.run();
    }

}
