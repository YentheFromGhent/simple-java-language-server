package com.yenthefromghent.sjls.core.lsp.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.yenthefromghent.sjls.core.lsp.error.ResponseError;
import com.yenthefromghent.sjls.core.lsp.types.AbstractResult;
import com.yenthefromghent.sjls.core.lsp.types.InitializeError;

public class ResponseMessage extends Message {

    /**
     * RepsonsMessage type
     */
    public Integer id;
    //only one of these can be present, other one will be null;
    public AbstractResult result;
    public ResponseError error;

    public ResponseMessage(Integer id, ResponseError error) {
        this.id = id;
        this.error = error;
    }

    public ResponseMessage(Integer id, AbstractResult result) {
        this.id = id;
        this.result = result;
    }

    public ResponseMessage(ResponseError error) {
        this.error = error;
    }

    public ResponseMessage(Integer id) {
        this.id = id;
    }

}
