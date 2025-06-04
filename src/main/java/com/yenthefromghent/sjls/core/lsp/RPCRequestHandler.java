package com.yenthefromghent.sjls.core.lsp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yenthefromghent.sjls.core.lsp.error.ErrorCode;
import com.yenthefromghent.sjls.core.lsp.error.ResponseError;
import com.yenthefromghent.sjls.core.lsp.message.ResponseMessage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RPCRequestHandler {

    private static final Logger LOGGER = Logger.getLogger("main");
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private static boolean isInitialized = false;

    /**
     * function that takes a RPC call, and will direct it to the correct handler,
     * or ignore the call all together
     * @param request message byte[]
     */
    public void handleRPCCall(byte[] request) {
        JsonNode RPCObject = requestToJson(request);

        JsonNode id = RPCObject.get("id");
        String methodName = RPCObject.get("method").asText();

        if (methodName.startsWith("/$")) {
            //We do not handle '/$' methods currently
            return;
        }

        //If we have no id, it is a notification
        if (id == null) {
            LOGGER.finest("Request is notification");
            handleNotification(RPCObject);
            return;
        }

        LOGGER.finest("Request received");
        handleRequest(RPCObject, id.asInt());
    }

    /**
     * function that handles notification messages
     * @param notificationObject the json message object
     */
    public void handleNotification(JsonNode notificationObject) {
        //If we have not yet intialized, we ignore notifications
        String methodName = notificationObject.get("method").asText();

        if (!isInitialized && !methodName.equals("intitialized")) {
            return;
        }

        LOGGER.finest("Invoking notification method: " + methodName);
        RPCMethodRegistery.invokeMethod(methodName);
    }

    /**
     * function that handles request messages
     * @param requestObject the json message object
     */
    public void handleRequest(JsonNode requestObject, int id) {
        String methodName = requestObject.get("method").asText();
        if (!isInitialized && !methodName.equals("initialize")) {
            LSP.send(new ResponseMessage(
                    new ResponseError(ErrorCode.REQUESTBEFOREINI, "Request before initialization")
            ));
        }

        LOGGER.finest("Trying to parse params");
        JsonNode paramsNode = requestObject.get("params");
        //TODO parse parameters given with request

        try {
            RPCMethodRegistery.invokeMethodWithId(methodName, id,"wow", "wow");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "RPC method " + methodName + " threw an exception: ", e);
        }
    }

    /**
     * helper function to turn our byte[] into a JsonNode using Jackson
     * @param request the message byte[]
     * @return JsonNode object
     */
    public JsonNode requestToJson(byte[] request) {
        try {
           return MAPPER.readTree(request);
        } catch (JsonProcessingException JsonMappingException) {
            LOGGER.severe("Error parsing request: " + new String(request));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void setIsInitialized(boolean isInitialized) {
        RPCRequestHandler.isInitialized = isInitialized;
    }

}
