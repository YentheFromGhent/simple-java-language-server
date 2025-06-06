package com.yenthefromghent.sls.test;

import com.yenthefromghent.sls.core.io.StdInRpcRequestReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class StdInRpcRequestReaderTest {

    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        // Save original System.in so we can restore it after
        originalIn = System.in;
    }

    @AfterEach
    void tearDown() {
        // Restore original System.in
        System.setIn(originalIn);
    }

    @Test
    void testNextMessage_smallMessage() throws IOException {
        // Given
        String jsonBody = "{\"jsonrpc\":\"2.0\",\"method\":\"ping\",\"id\":1}";
        String fullMessage = buildFullRpcMessage(jsonBody);
        System.setIn(new ByteArrayInputStream(fullMessage.getBytes(StandardCharsets.UTF_8)));

        StdInRpcRequestReader reader = new StdInRpcRequestReader();

        // When
        byte[] messageBytes = reader.nextMessage();
        String message = new String(messageBytes, StandardCharsets.UTF_8);

        // Then
        assertEquals(jsonBody, message);
    }

    @Test
    void testNextMessage_largeMessage() throws IOException {
        // Given: build a very large JSON body
        StringBuilder sb = new StringBuilder();
        sb.append("{\"jsonrpc\":\"2.0\",\"method\":\"large\",\"data\":\"");
        for (int i = 0; i < 10_000; i++) {
            sb.append("x");
        }
        sb.append("\",\"id\":2}");

        String jsonBody = sb.toString();
        String fullMessage = buildFullRpcMessage(jsonBody);

        System.setIn(new ByteArrayInputStream(fullMessage.getBytes(StandardCharsets.UTF_8)));

        StdInRpcRequestReader reader = new StdInRpcRequestReader();

        // When
        byte[] messageBytes = reader.nextMessage();
        String message = new String(messageBytes, StandardCharsets.UTF_8);

        // Then
        assertEquals(jsonBody, message);
    }

    @Test
    void testNextMessage_partialReads() throws IOException {
        // Simulate message arriving in chunks
        String jsonBody = "{\"jsonrpc\":\"2.0\",\"method\":\"chunked\",\"id\":3}";
        String fullMessage = buildFullRpcMessage(jsonBody);

        // Split the message into chunks to simulate partial arrival
        byte[][] chunks = {
                fullMessage.substring(0, 10).getBytes(StandardCharsets.UTF_8),
                fullMessage.substring(10, 25).getBytes(StandardCharsets.UTF_8),
                fullMessage.substring(25).getBytes(StandardCharsets.UTF_8)
        };

        // Wrap these chunks into an InputStream that feeds them one at a time
        InputStream chunkedInputStream = new SequenceInputStream(new ByteArrayInputStream(chunks[0]),
                new SequenceInputStream(new ByteArrayInputStream(chunks[1]), new ByteArrayInputStream(chunks[2])));

        System.setIn(chunkedInputStream);

        StdInRpcRequestReader reader = new StdInRpcRequestReader();

        // When
        byte[] messageBytes = reader.nextMessage();
        String message = new String(messageBytes, StandardCharsets.UTF_8);

        // Then
        assertEquals(jsonBody, message);
    }

    /**
     * Helper method to build a full RPC message with Content-Length header.
     */
    private String buildFullRpcMessage(String jsonBody) {
        int contentLength = jsonBody.getBytes(StandardCharsets.UTF_8).length;
        return String.format("Content-Length: %d\r\n\r\n%s", contentLength, jsonBody);
    }
}
