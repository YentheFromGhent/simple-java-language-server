package com.yenthefromghent.jsls;

import com.yenthefromghent.sjls.core.lsp.LSP;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Small test class to test features and debug
 */
public class LSPTest {

    @Test
    void testEncodeMessage() throws IOException {
        var obj = new TestMessage("hello", 42);
        String encoded = LSP.encodeMessage(obj);

        assertTrue(encoded.startsWith("Content-Length: "));
        assertTrue(encoded.contains("{\"message\":\"hello\",\"id\":42}"));
        // Check if Content-Length matches the actual length of JSON body
        String[] parts = encoded.split("\r\n\r\n");
        assertEquals(parts[1].getBytes(StandardCharsets.UTF_8).length, Integer.parseInt(parts[0].substring("Content-Length: ".length())));
    }

    @Test
    void testReadNextMessageSingle() throws IOException {
        // Prepare an LSP message
        var obj = new TestMessage("test", 1);
        String encoded = LSP.encodeMessage(obj);
        byte[] bytes = encoded.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        LSP.StdinMessageDecoder decoder = new LSP.StdinMessageDecoder();
        byte[] message = decoder.readNextMessage(inputStream);

        String messageStr = new String(message, StandardCharsets.UTF_8);
        assertTrue(messageStr.contains("\"message\":\"test\""));
        assertTrue(messageStr.contains("\"id\":1"));
    }

    @Test
    void testReadNextMessageMultiple() throws IOException {
        var obj1 = new TestMessage("first", 1);
        var obj2 = new TestMessage("second", 2);

        String encoded1 = LSP.encodeMessage(obj1);
        String encoded2 = LSP.encodeMessage(obj2);

        // Combine messages to simulate a stream with multiple messages
        byte[] combined = (encoded1 + encoded2).getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(combined);

        LSP.StdinMessageDecoder decoder = new LSP.StdinMessageDecoder();

        byte[] message1 = decoder.readNextMessage(inputStream);
        byte[] message2 = decoder.readNextMessage(inputStream);

        assertNotNull(message1);
        assertNotNull(message2);

        String msg1Str = new String(message1, StandardCharsets.UTF_8);
        String msg2Str = new String(message2, StandardCharsets.UTF_8);

        assertTrue(msg1Str.contains("\"message\":\"first\""));
        assertTrue(msg2Str.contains("\"message\":\"second\""));
    }

    @Test
    void testDecodeMessageProcessesAll() throws IOException {
        var obj1 = new TestMessage("hello", 1);
        var obj2 = new TestMessage("world", 2);

        String combined = LSP.encodeMessage(obj1) + LSP.encodeMessage(obj2);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(combined.getBytes(StandardCharsets.UTF_8));

        // Just call decodeMessage, it will call handleRequest internally.
        // We want to ensure no exceptions are thrown.
        LSP.decodeMessage(inputStream);
    }

    // Helper class for test messages
    static class TestMessage {
        public String message;
        public int id;

        public TestMessage() {}

        public TestMessage(String message, int id) {
            this.message = message;
            this.id = id;
        }
    }
}
