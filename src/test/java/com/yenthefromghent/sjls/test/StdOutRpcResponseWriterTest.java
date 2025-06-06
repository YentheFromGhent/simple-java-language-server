package com.yenthefromghent.sjls.test;

import com.yenthefromghent.sjls.core.io.StdOutRpcResponseWriter;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class StdOutRpcResponseWriterTest {

    @Test
    void testWriteMessage_smallMessage() {
        String jsonBody = "{\"jsonrpc\":\"2.0\",\"result\":\"pong\",\"id\":1}";
        String fullMessage = buildFullRpcMessage(jsonBody);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        StdOutRpcResponseWriter writer = new StdOutRpcResponseWriter(outContent);

        writer.writeMessage(fullMessage.getBytes(StandardCharsets.UTF_8));

        String writtenOutput = outContent.toString(StandardCharsets.UTF_8);
        assertEquals(fullMessage, writtenOutput);
    }

    @Test
    void testWriteMessage_largeMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"jsonrpc\":\"2.0\",\"result\":\"");
        sb.append("x".repeat(10_000));
        sb.append("\",\"id\":2}");

        String jsonBody = sb.toString();
        String fullMessage = buildFullRpcMessage(jsonBody);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        StdOutRpcResponseWriter writer = new StdOutRpcResponseWriter(outContent);

        writer.writeMessage(fullMessage.getBytes(StandardCharsets.UTF_8));

        String writtenOutput = outContent.toString(StandardCharsets.UTF_8);
        assertEquals(fullMessage, writtenOutput);
    }

    @Test
    void testWriteMessage_multipleMessages() {
        String jsonBody1 = "{\"jsonrpc\":\"2.0\",\"result\":\"one\",\"id\":1}";
        String jsonBody2 = "{\"jsonrpc\":\"2.0\",\"result\":\"two\",\"id\":2}";

        String fullMessage1 = buildFullRpcMessage(jsonBody1);
        String fullMessage2 = buildFullRpcMessage(jsonBody2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        StdOutRpcResponseWriter writer = new StdOutRpcResponseWriter(outContent);

        writer.writeMessage(fullMessage1.getBytes(StandardCharsets.UTF_8));
        writer.writeMessage(fullMessage2.getBytes(StandardCharsets.UTF_8));

        String writtenOutput = outContent.toString(StandardCharsets.UTF_8);
        assertEquals(fullMessage1 + fullMessage2, writtenOutput);
    }

    @Test
    void testWriteMessage_ioException_logged() {
        // Simulate an OutputStream that throws IOException
        BrokenOutputStream brokenOut = new BrokenOutputStream();

        // Capture logger output
        Logger logger = Logger.getLogger("main");
        TestLogHandler testLogHandler = new TestLogHandler();
        logger.addHandler(testLogHandler);
        logger.setLevel(Level.ALL);

        try {
            StdOutRpcResponseWriter writer = new StdOutRpcResponseWriter(brokenOut);

            writer.writeMessage("Some message".getBytes(StandardCharsets.UTF_8));

            assertFalse(testLogHandler.logMessages.isEmpty());
        } finally {
            logger.removeHandler(testLogHandler);
        }
    }

    /**
     * Helper method to build a full RPC message with Content-Length header.
     */
    private String buildFullRpcMessage(String jsonBody) {
        int contentLength = jsonBody.getBytes(StandardCharsets.UTF_8).length;
        return String.format("Content-Length: %d\r\n\r\n%s", contentLength, jsonBody);
    }

    /**
     * A BrokenOutputStream that always throws IOException on write or flush.
     */
    static class BrokenOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            throw new IOException("Simulated IOException in write()");
        }

        @Override
        public void flush() throws IOException {
            throw new IOException("Simulated IOException in flush()");
        }
    }

    /**
     * A simple log handler to capture log messages for testing.
     */
    static class TestLogHandler extends Handler {

        private final StringBuilder logMessages = new StringBuilder();

        @Override
        public void publish(LogRecord record) {
            logMessages.append(record.getLevel()).append(": ").append(record.getMessage()).append("\n");
        }

        @Override
        public void flush() {}

        @Override
        public void close() throws SecurityException {}

    }
}
