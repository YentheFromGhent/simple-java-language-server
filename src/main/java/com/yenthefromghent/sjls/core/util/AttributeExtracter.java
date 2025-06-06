package com.yenthefromghent.sjls.core.util;

import com.google.gson.JsonObject;

public interface AttributeExtracter {

    JsonObject extractAttribute(JsonObject jsonObject, String attribute);

    String extractAttributeAsString(JsonObject jsonObject, String attribute);

}
