package com.yenthefromghent.jlsp.core.lsp.types;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.util.List;

public class InitializeParams {

    /**
     * class to define the structure of InitializeParams
     */
    public Integer processId;
    public String rootPath;
    public URI documentUri;
    public JsonNode initializationOptions;
    public Capabilities capabilities;
    public List<WorkspaceFolder> workspaceFolders;

}
