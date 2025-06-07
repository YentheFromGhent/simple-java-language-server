package com.yenthefromghent.sjls.core.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class will read constantly from stdin looking from rpc-messages
 */
public class StdInRpcRequestReader implements RpcRequestReader {

    private static final Logger LOGGER = Logger.getLogger("reader");

    public StdInRpcRequestReader() {
        LOGGER.finest("initializing StdInRpcRequestReader");
    }

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final byte[] buffer = new byte[4096];

    public byte[] nextMessage() throws IOException {
        LOGGER.log(Level.FINEST, "reading next message");

        while (true) {
            int bytesRead = System.in.read(buffer);
            if (bytesRead == -1 && byteArrayOutputStream.size() == 0) { return null; } //EOF

            if (bytesRead > 0 ) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] currentBytes = byteArrayOutputStream.toByteArray();

            int headerEndIndex = findControlReturnSequence(currentBytes);
            if (headerEndIndex != -1) {

                int contentLength = extractContentLength(new String(currentBytes, 0, headerEndIndex));
                int totalLength = contentLength + headerEndIndex;

                if (totalLength <= currentBytes.length) {
                    byte[] contentMessage = Arrays.copyOfRange(currentBytes, headerEndIndex, totalLength);
                    byte[] remainingBytes = Arrays.copyOfRange(currentBytes, totalLength, currentBytes.length);

                    byteArrayOutputStream.reset();
                    byteArrayOutputStream.write(remainingBytes);

                    return contentMessage;
                }
            }
            //this is necesarry, if we had no bytes read, and the remaing stored bytes did not contain a request.
            if (bytesRead == -1) { return null; }
        }
    }


    private int findControlReturnSequence(byte[] data) {
        for (int i = 0; i < data.length - 3; i++) {
            if (data[i] == '\r' && data[i + 1] == '\n' && data[i + 2] == '\r' && data[i + 3] == '\n') {
                return i + 4;
            }
        }
        LOGGER.log(Level.FINEST, "control sequence not found");
        return -1;
    }

    private int extractContentLength(String header) {
        for (String line : header.split("\r\n")) {
            if (line.startsWith("Content-Length: ")) {
                return Integer.parseInt(line.substring("Content-Length: ".length()).trim());
            }
        }
        LOGGER.log(Level.SEVERE, "content length not found when in message containg \\r\\n\\r\\n");
        throw new IllegalArgumentException("Missing Content-Length header");
    }

}
