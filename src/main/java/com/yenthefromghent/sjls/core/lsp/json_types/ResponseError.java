package com.yenthefromghent.sjls.core.lsp.json_types;

import com.google.gson.JsonObject;

public class ResponseError {

    // Error code to return
    public Integer code;
    public String message;
    public JsonObject data; // This can contain extra info about the error

    public ResponseError(Integer code, String message, JsonObject errorMessageJsonObject) {
        this.code = code;
        this.message = message;
        this.data = errorMessageJsonObject;
    }

    public ResponseError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
