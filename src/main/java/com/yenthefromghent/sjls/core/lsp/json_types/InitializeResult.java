package com.yenthefromghent.sjls.core.lsp.json_types;

public class InitializeResult {

    public Capabilities capabilities;
    public ServerInfo serverInfo;

    public InitializeResult(Capabilities capabilities, ServerInfo serverInfo) {
        this.capabilities = capabilities;
        this.serverInfo = serverInfo;
    }
}
