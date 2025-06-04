package com.yenthefromghent.sjls.core.lsp.error;

public class ResponseError {

    /**
     * Error type
     */
    public Integer id;
    public String message;
    public AbstractError LSPAny; //can be null

    public ResponseError(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

    public ResponseError(Integer id, String message, AbstractError LSPAny) {
        this.id = id;
        this.message = message;
        this.LSPAny = LSPAny;
    }

}
