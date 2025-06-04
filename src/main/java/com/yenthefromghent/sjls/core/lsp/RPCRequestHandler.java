package com.yenthefromghent.sjls.core.lsp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RPCRequestHandler {

    private static final Logger LOGGER = Logger.getLogger("main");
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public void handleRequest(byte[] request) {
        JsonNode requestObject = requestToJson(request);

        String method = requestObject.get("method").asText();
        LOGGER.log(Level.FINEST, "Request: " + method);

        JsonNode params = requestObject.get("params");
        LOGGER.log(Level.FINEST, "Params: " + params);
        //TODO after getting method, check if it exist. of not send response, otherwise execute method.
    }

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


    public <T> void reply(T response) {
        LSP.send(response);
    }

}
