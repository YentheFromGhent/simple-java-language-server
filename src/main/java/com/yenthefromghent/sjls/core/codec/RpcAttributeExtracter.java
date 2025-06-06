package com.yenthefromghent.sjls.core.codec;

import com.google.gson.JsonObject;

public class RpcAttributeExtracter implements AttributeExtracter {

    public JsonObject extractAttribute(JsonObject jsonObject, String attribute) {
        return jsonObject.get(attribute).getAsJsonObject();
    }

    public String extractAttributeAsString(JsonObject jsonObject, String attribute) {
        return jsonObject.get(attribute).getAsString();
    }

}
