package com.yenthefromghent.sjls.core.codec;

import com.google.gson.JsonObject;

public interface MessageCodec {

    JsonObject decode(byte[] message);

    byte[] encode(JsonObject json);

}
