package com.yenthefromghent.sjls.core.lsp.message;

import com.fasterxml.jackson.databind.JsonNode;

public class RequestMessage extends Message {

    public Integer id;
    public String method;
    public JsonNode params; //can be null

}
