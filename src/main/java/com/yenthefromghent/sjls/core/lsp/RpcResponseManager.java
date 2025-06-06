package com.yenthefromghent.sjls.core.lsp;

import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.io.RpcResponseWriter;
import com.yenthefromghent.sjls.core.io.StdOutRpcResponseWriter;
import com.yenthefromghent.sjls.core.lsp.json_types.ResponseError;
import com.yenthefromghent.sjls.core.lsp.json_types.ResponseMessage;
import com.yenthefromghent.sjls.core.util.JsonObjectCreater;
import com.yenthefromghent.sjls.core.util.MessageCodec;
import com.yenthefromghent.sjls.core.util.RPCMessageCodec;
import com.yenthefromghent.sjls.extra.Error;

/* utility class we can use to send a response to the client */
public class RpcResponseManager {

    private static final RpcResponseWriter outWriter = new StdOutRpcResponseWriter();
    private static final JsonObjectCreater jsonCreater = new JsonObjectCreater();
    private static final MessageCodec codec = new RPCMessageCodec();

    // These methods all do the same thing, but just take different paramaters to make it easier
    // to send certain kind of responses

    public static <T> void sendResponse(T response, int id) {
        ResponseMessage responseMessage = createRepsonseMessage(response, id);
        byte[] responseBytes = codec.encode(responseMessage);
        outWriter.writeMessage(responseBytes);
    }

    public static <T> void sendErrorResponse(int id, int errorCode, String errorMessage, T errorData) {
        ResponseMessage responseMessage = createErrorMessage(id, errorCode, errorMessage, errorData);
        byte[] reponsebytes = codec.encode(responseMessage);
        outWriter.writeMessage(reponsebytes);
    }

    // If no error code is given, we assume it is a unknow error.
    public static <T> void sendErrorResponse(int id, String errorMessage, T errorData) {
        ResponseMessage responseMessage = createErrorMessage(id, Error.UNKNOWN_ERRORCODE.error(), errorMessage, errorData);
        byte[] reponsebytes = codec.encode(responseMessage);
        outWriter.writeMessage(reponsebytes);
    }

    // ErrorResponse without data
    public static void sendErrorResponse(int id, int errorCode, String errorMessage) {
        ResponseMessage responseMessage = createErrorMessage(id, errorCode, errorMessage);
        byte[] reponsebytes = codec.encode(responseMessage);
        outWriter.writeMessage(reponsebytes);
    }

    public static void sendErrorResponse(int errorCode, String errorMessage) {
        ResponseMessage responseMessage = createErrorMessage(errorCode, errorMessage);
        byte[] reponsebytes = codec.encode(responseMessage);
        outWriter.writeMessage(reponsebytes);
    }

    private static <T> ResponseMessage createRepsonseMessage(T response, int id) {
        JsonObject repsonseJsonObject = jsonCreater.toJsonObject(response);
        return new ResponseMessage(id, repsonseJsonObject);
    }

    private static <T> ResponseMessage createErrorMessage(int id, int errorCode, String errorMessage, T errorData) {
        JsonObject errorMessageJsonObject = jsonCreater.toJsonObject(errorData);
        ResponseError error = new ResponseError(errorCode, errorMessage, errorMessageJsonObject);
        return new ResponseMessage(id, error);
    }

    private static ResponseMessage createErrorMessage(int id, int errorCode, String errorMessage) {
        ResponseError error = new ResponseError(errorCode, errorMessage);
        return new ResponseMessage(id, error);
    }

    private static ResponseMessage createErrorMessage(int errorCode, String errorMessage) {
        ResponseError error = new ResponseError(errorCode, errorMessage);
        return new ResponseMessage(error);
    }

}