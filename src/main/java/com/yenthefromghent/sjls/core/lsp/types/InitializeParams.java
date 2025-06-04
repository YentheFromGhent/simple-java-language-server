package com.yenthefromghent.sjls.core.lsp.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InitializeParams {

    /**
     * class to define the structure of InitializeParams
     */
    public Integer processId;
    public String rootPath;
    public URI documentUri;
    public URI rootUri;
    public JsonNode initializationOptions;
    public Capabilities capabilities;
    public List<WorkspaceFolder> workspaceFolders;

}
