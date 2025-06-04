package com.yenthefromghent.sjls.core.lsp.types;

public class InitializeResult extends AbstractResult {

    /**
     * defines the initialization results
     */
    public Capabilities capabilities;
    public ServerInfo serverInfo;

    public InitializeResult(Capabilities capabilities, ServerInfo serverInfo) {
        this.capabilities = capabilities;
        this.serverInfo = serverInfo;
    }

}
