package com.yenthefromghent.sjls.core.server.lsp.dto;

import com.google.gson.JsonObject;

public class ResponseMessage extends Message {

    public Integer id;
    // Only one of these two should be present at once
    public JsonObject result;
    public ResponseError error;

    public ResponseMessage(Integer id, ResponseError error) {
        this.id = id;
        this.error = error;
    }

    public ResponseMessage(ResponseError error) {
        this.error = error;
    }

    public ResponseMessage(Integer id, JsonObject response) {
        this.id = id;
        this.result = response;
    }

    public ResponseMessage(Integer id) {
        this.id = id;
    }

}
