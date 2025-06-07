package com.yenthefromghent.sjls.core.lsp.json_types;

import com.google.gson.JsonElement;
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

}
