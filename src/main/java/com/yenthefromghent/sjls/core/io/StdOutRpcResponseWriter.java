package com.yenthefromghent.sjls.core.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class StdOutRpcResponseWriter implements RpcResponseWriter {

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

        LOGGER.finest("Outgoing lsp message (hex):\n " + bytesToHex(response));

        try {
            out.write(response);
            out.flush();
        } catch (IOException e) {
            LOGGER.warning("Failed to write response to stdout with error: " + e.getMessage());
        }

        LOGGER.finest("Message written");
    }


    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x ", b));
        }
        return hex.toString().trim();
    }

}
