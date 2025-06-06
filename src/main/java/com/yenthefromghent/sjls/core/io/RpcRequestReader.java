package com.yenthefromghent.sjls.core.io;

import java.io.IOException;

@FunctionalInterface
public interface RpcRequestReader {

    byte[] nextMessage() throws IOException;

}
