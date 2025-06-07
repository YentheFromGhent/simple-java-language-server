package com.yenthefromghent.sjls;

import com.yenthefromghent.sjls.core.cli.CommandlineOptionsParser;
import com.yenthefromghent.sjls.core.cli.CommandLineParams;
import com.yenthefromghent.sjls.core.lsp.Lsp;
import com.yenthefromghent.sjls.core.server.Server;
import com.yenthefromghent.sjls.extra.LogOptions;

public class Main {

    public static void main(String[] args) {
        new Server(args);
    }

}
