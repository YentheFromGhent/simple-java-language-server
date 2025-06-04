package com.yenthefromghent.sjls.core.lsp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LSP class
 * Central class to encode / decode message
 */
public class LSP {

    private static final ObjectMapper jsonObjectMapper = new ObjectMapper();
    static final Logger LOGGER = Logger.getLogger("main");

    public static <T> String encodeMessage (T message) {
        try {
            var messageText = jsonObjectMapper.writeValueAsString(message);
            return String.format("Content-Length: %d\r\n\r\n%s", messageText.getBytes().length, messageText);
        } catch (JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public static void decodeMessage(InputStream client) throws IOException {
        StdinMessageDecoder stdinMessageDecoder = new StdinMessageDecoder();

        LOGGER.log(Level.INFO, "starting to read from stdin");

        byte[] messageBytes;
        while ((messageBytes = stdinMessageDecoder.readNextMessage(client)) != null) {
            handleRequest(messageBytes);
        }

        LOGGER.log(Level.INFO, "finished reading from stdin");
    }

    private static final RPCRequestHandler RPC_REQUEST_HANDLER = new RPCRequestHandler();

    public static void handleRequest(byte[] request) {
        LOGGER.log(Level.FINEST, "received request");
        RPC_REQUEST_HANDLER.handleRequest(request);
    }

    public static void run() throws IOException {
        decodeMessage(System.in);
        LOGGER.log(Level.FINEST, "closing server");
    }

    private static final PrintStream out = new PrintStream(System.out, true);

    public static <T> void send(T responseObject) {
        LOGGER.log(Level.FINEST, "sending message");
        String responseMessage = encodeMessage(responseObject);

        out.println(responseMessage);
    }

    public static class StdinMessageDecoder {

        private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private final byte[] buffer = new byte[4096];

        public byte[] readNextMessage(InputStream in) throws IOException {
            LOGGER.log(Level.FINEST, "reading next message");
            while (true) {
                int bytesRead = in.read(buffer);
                if (bytesRead == -1 && byteArrayOutputStream.size() == 0) { return null; } // EOF

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

}
