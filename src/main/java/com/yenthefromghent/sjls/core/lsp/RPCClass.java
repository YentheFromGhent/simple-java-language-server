package com.yenthefromghent.sjls.core.lsp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;

public abstract class RPCClass {

    public static final Logger LOGGER = Logger.getLogger("main");

    public static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        RPCRequestHandler.MAPPER.configure(
                com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
        );
    }

    public <T> T deserialize(JsonNode json, Class<T> classInstance) {
        try {
            T params = MAPPER.convertValue(json, classInstance);
            LOGGER.finest("parsed params: " + params);
            return params;
        } catch (Exception e) {
            LOGGER.severe("Error while parsing InitializeParams: " + e.getMessage());
            return null;
        }
    }

}
