package com.yenthefromghent.sls.core.codec;

import com.google.gson.JsonObject;

public interface MessageCodec {

    JsonObject decode(byte[] message);

    byte[] encode(JsonObject json);

}
