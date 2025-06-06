package com.yenthefromghent.sjls.core.util;

import com.google.gson.JsonObject;

public interface MessageCodec {

    JsonObject decode(byte[] message);

    <T> byte[] encode(T json);

}
