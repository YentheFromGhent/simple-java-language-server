package com.yenthefromghent.sjls.core.codec;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class RPCMessageCodec implements MessageCodec {

    private static final Logger LOGGER = Logger.getLogger("main");
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
    public byte[] encode(JsonObject json) {
        String jsonString = json.toString();
        return String.format(
                "Content-Length: %d\r\n\r\n%s", jsonString.getBytes().length, jsonString)
                .getBytes(StandardCharsets.UTF_8);
    }
}
