package com.yenthefromghent.sjls.core.server.lsp.dto;

public class InitializeResult {

    public Capabilities capabilities;
    public ServerInfo serverInfo;

    public InitializeResult(Capabilities capabilities, ServerInfo serverInfo) {
        this.capabilities = capabilities;
        this.serverInfo = serverInfo;
    }
}
