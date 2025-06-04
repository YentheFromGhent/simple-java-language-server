package com.yenthefromghent.sjls.core.lsp.error;

import com.fasterxml.jackson.databind.JsonNode;

public class ResponseError {

    /**
     * Error type
     */
    public Integer id;
    public String message;
    public JsonNode LSPAny; //can be null

}
