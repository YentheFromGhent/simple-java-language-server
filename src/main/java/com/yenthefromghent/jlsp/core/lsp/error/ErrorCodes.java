package com.yenthefromghent.jlsp.core.lsp.error;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodes {

    public static Map<String, Integer> errorCodes = new HashMap<>();

    static {
        errorCodes.put("ParseError", -32700);
        errorCodes.put("InvalidRequest", -32600);
        errorCodes.put("MethodNotFound", -32601);
        errorCodes.put("InvalidParams", -32602);
        errorCodes.put("InternalError", -32603);
    }

}
