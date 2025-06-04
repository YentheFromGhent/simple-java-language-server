package com.yenthefromghent.sjls.core.lsp.message;

import com.fasterxml.jackson.databind.JsonNode;

public class NotificationMessage extends Message {

    public String method;
    public JsonNode params; //can be null

}
