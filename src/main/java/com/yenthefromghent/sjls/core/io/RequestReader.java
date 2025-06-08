package com.yenthefromghent.sjls.core.io;

import java.io.IOException;

@FunctionalInterface
public interface RequestReader {

    byte[] nextMessage() throws IOException;

}
