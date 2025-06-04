package com.yenthefromghent.sjls.core.lsp.message;

import com.fasterxml.jackson.databind.JsonNode;

public class ResponseMessage extends Message {

    /**
     * RepsonsMessage type
     */
    public Integer id;
    //only one of these can be present, other one will be null;
    public JsonNode result;
    public JsonNode error;

}
