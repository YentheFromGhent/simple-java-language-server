package com.yenthefromghent.jsls;

import com.yenthefromghent.sjls.core.lsp.LSP;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Big test class that should be run before commting to main
 */
public class BigLSPTest {

    record MockMessage(String json) {}

    private String wrapWithLspHeader(String json) {
        int len = json.getBytes().length;
        return "Content-Length: " + len + "\r\n\r\n" + json;
    }

    @Test
    public void testMultipleMessagesInSingleStream() throws Exception {
        // Construct several mock JSON-RPC messages
        String json1 = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}";
        String json2 = "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"initialized\",\"params\":{}}";
        String json3 = "{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"textDocument/didOpen\",\"params\":{}}";

        // Wrap them all in LSP protocol headers
        String combined = wrapWithLspHeader(json1)
                + wrapWithLspHeader(json2)
                + wrapWithLspHeader(json3);

        InputStream stream = new ByteArrayInputStream(combined.getBytes());

        // Use your decoder to read messages
        LSP.StdinMessageDecoder decoder = new LSP.StdinMessageDecoder();
        List<String> decodedMessages = new ArrayList<>();

        byte[] msg;
        while ((msg = decoder.readNextMessage(stream)) != null) {
            decodedMessages.add(new String(msg));
        }

        // Assertions
        assertEquals(3, decodedMessages.size());
        assertTrue(decodedMessages.get(0).contains("\"method\":\"initialize\""));
        assertTrue(decodedMessages.get(1).contains("\"method\":\"initialized\""));
        assertTrue(decodedMessages.get(2).contains("\"method\":\"textDocument/didOpen\""));
    }

    @Test
    public void testThousandMessagesInStream() throws Exception {
        String baseJson = "{\"jsonrpc\":\"2.0\",\"id\":%d,\"method\":\"ping\",\"params\":{}}";
        StringBuilder combined = new StringBuilder();

        // Generate 1000 LSP-wrapped messages
        for (int i = 0; i < 1000; i++) {
            String msg = String.format(baseJson, i);
            combined.append(wrapWithLspHeader(msg));
        }

        InputStream stream = new ByteArrayInputStream(combined.toString().getBytes());
        LSP.StdinMessageDecoder decoder = new LSP.StdinMessageDecoder();
        int count = 0;

        byte[] message;
        while ((message = decoder.readNextMessage(stream)) != null) {
            String decoded = new String(message);
            assertTrue(decoded.contains("\"method\":\"ping\""));
            count++;
        }

        assertEquals(1000, count);
    }

    @Test
    public void testVeryLargeSingleMessage() throws Exception {
        // Generate a large dummy payload (e.g., simulating a large text document)
        StringBuilder largeParam = new StringBuilder();
        largeParam.append("{\"jsonrpc\":\"2.0\",\"id\":99,\"method\":\"textDocument/didChange\",\"params\":{\"text\":\"");
        largeParam.append("A".repeat(50000)); // 50 KB of 'A'
        largeParam.append("\"}}");

        String fullMessage = wrapWithLspHeader(largeParam.toString());
        InputStream stream = new ByteArrayInputStream(fullMessage.getBytes());

        LSP.StdinMessageDecoder decoder = new LSP.StdinMessageDecoder();
        byte[] message = decoder.readNextMessage(stream);

        assertNotNull(message);
        String decoded = new String(message);
        System.out.println(decoded);
        assertTrue(decoded.contains("\"method\":\"textDocument/didChange\""));
        assertTrue(decoded.contains("\"" + "A".repeat(100))); // sanity check
    }



}
