package com.yenthefromghent.sjls.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonObjectCreater {

    private final Gson GSON = new Gson();

    public <T> JsonObject toJsonObject(T object) {
        return GSON.toJsonTree(object).getAsJsonObject();
    }

}
