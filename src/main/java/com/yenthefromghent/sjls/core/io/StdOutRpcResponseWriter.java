package com.yenthefromghent.sjls.core.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/** will send a buffered response to std-out **/
public class StdOutRpcResponseWriter implements ResponseWriter {

    private final BufferedOutputStream out;
    private static final Logger LOGGER = Logger.getLogger("main");

    public StdOutRpcResponseWriter() {
        this(System.out);
        LOGGER.finest("initializing StdOutRpcResponseWriter");
    }

    //Constructor used for testing.
    public StdOutRpcResponseWriter(OutputStream outputStream) {
        this.out = new BufferedOutputStream(outputStream);
    }

    @Override
    public void writeMessage(byte[] response) {
        LOGGER.finest("Writing ressponse to stdout: " + new String(response));
        try {
            out.write(response);
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Failed to write response to stdout with error: " + e.getMessage());
        }
        LOGGER.finest("Message written");
    }


    // Helper method that turns a byte array, into it's hex form. this can be usefull for debugging.
    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x ", b));
        }
        return hex.toString().trim();
    }

}
