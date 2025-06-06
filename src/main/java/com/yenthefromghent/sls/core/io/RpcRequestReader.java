package com.yenthefromghent.sls.core.io;

import java.io.IOException;

@FunctionalInterface
public interface RpcRequestReader {

    byte[] nextMessage() throws IOException;

}
