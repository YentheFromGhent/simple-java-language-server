package com.yenthefromghent.sjls.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class RPCMessageCodec implements MessageCodec {

    private static final Logger LOGGER = Logger.getLogger("main");
    private static final Gson GSON = new Gson();

    @Override
    public JsonObject decode(byte[] json) {
        //Turn the byte[] into a string
        String jsonString = new String(json, StandardCharsets.UTF_8);

        try {
            return JsonParser.parseString(jsonString).getAsJsonObject();
        } catch (Exception e) {
            LOGGER.severe("Error while parsing json: " + jsonString);
        }
        return null;
    }

    @Override
    public <T> byte[] encode(T json) {
        String jsonString = GSON.toJson(json);
        return String.format(
                "Content-Length: %d\r\n\r\n%s", jsonString.getBytes().length, jsonString)
                .getBytes(StandardCharsets.UTF_8);
    }
}
