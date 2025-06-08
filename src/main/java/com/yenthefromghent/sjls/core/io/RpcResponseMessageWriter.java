package com.yenthefromghent.sjls.core.io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yenthefromghent.sjls.core.server.lsp.dto.ResponseError;
import com.yenthefromghent.sjls.core.server.lsp.dto.ResponseMessage;
import com.yenthefromghent.sjls.core.util.MessageCodec;
import com.yenthefromghent.sjls.core.util.RPCMessageCodec;
import com.yenthefromghent.sjls.extra.exception.Error;

/** utility class create and send a response to the client **/
public class RpcResponseMessageWriter {

    private static final ResponseWriter outWriter = new StdOutRpcResponseWriter();
    private static final MessageCodec codec = new RPCMessageCodec();

    // These methods all do the same thing, but just take different paramaters to make it easier
    // to send certain kind of responses

    public static <T> void sendResponse(T response, int id) {
        ResponseMessage responseMessage = createRepsonseMessage(response, id);
        byte[] responseBytes = codec.encode(responseMessage);
        outWriter.writeMessage(responseBytes);
    }

    public static void sendResponse(int id) {
        ResponseMessage responseMessage = new ResponseMessage(id);
        responseMessage.result = new JsonObject();
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
        JsonObject repsonseJsonObject = toJsonObject(response);
        return new ResponseMessage(id, repsonseJsonObject);
    }

    private static <T> ResponseMessage createErrorMessage(int id, int errorCode, String errorMessage, T errorData) {
        JsonObject errorMessageJsonObject = toJsonObject(errorData);
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

    private static final Gson GSON = new Gson();

    // Helper method to turn a object into its JsonObject form
    public static <T> JsonObject toJsonObject(T object) {
        return GSON.toJsonTree(object).getAsJsonObject();
    }

}