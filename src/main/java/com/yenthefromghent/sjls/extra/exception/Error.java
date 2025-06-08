package com.yenthefromghent.sjls.extra.exception;

/** errorcode's as defined by the lsp specification **/
public enum Error {

    PARSE_ERROR(-32700),
    INVALID_REQUEST(-32600),
    METHOD_NOT_FOUND(-32601),
    INVALID_PARAMS(-32602),
    INTERNAL_ERROR(-32603),
    SERVER_NOT_INITIALIZED(-32002),
    UNKNOWN_ERRORCODE(-32001),
    REQUEST_FAILED(-32803),
    SERVER_CANCELLED(-32802),
    CONTENT_MODIFIED(-32801);

    private final Integer ERROR_CODE;

    Error(Integer errorCode) {
        this.ERROR_CODE = errorCode;
    }

    public Integer error() {
        return ERROR_CODE;
    }

}
