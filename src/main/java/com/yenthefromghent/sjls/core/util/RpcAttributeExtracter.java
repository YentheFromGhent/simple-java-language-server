package com.yenthefromghent.sjls.core.util;

import com.google.gson.JsonObject;

public class RpcAttributeExtracter implements AttributeExtracter {

    public JsonObject extractAttribute(JsonObject jsonObject, String attribute) {
        return jsonObject.get(attribute).getAsJsonObject();
    }

    public String extractAttributeAsString(JsonObject jsonObject, String attribute) {
        if (jsonObject.has(attribute)) {
            return jsonObject.get(attribute).getAsString();
        }
        return null;
    }

}
