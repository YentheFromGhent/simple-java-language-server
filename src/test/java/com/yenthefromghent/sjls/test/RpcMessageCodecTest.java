package com.yenthefromghent.sjls.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yenthefromghent.sjls.core.codec.MessageCodec;
import com.yenthefromghent.sjls.core.codec.RPCMessageCodec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RpcMessageCodecTest {

    private final MessageCodec codec = new RPCMessageCodec();

    @Test
    public void testDecode_validRpcRequest() {
        // Given: a valid JSON-RPC request formatted as bytes
        String jsonBody = "{\"jsonrpc\":\"2.0\",\"method\":\"subtract\",\"params\":[42, 23],\"id\":1}";
        String fullMessage = String.format("Content-Length: %d\r\n\r\n%s", jsonBody.getBytes().length, jsonBody);
        byte[] inputBytes = fullMessage.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // Extract only the body portion to simulate what 'decode' expects
        int bodyIndex = fullMessage.indexOf("\r\n\r\n") + 4;
        byte[] jsonBytes = fullMessage.substring(bodyIndex).getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // When
        JsonObject result = codec.decode(jsonBytes);

        // Then
        assertNotNull(result);
        assertEquals("2.0", result.get("jsonrpc").getAsString());
        assertEquals("subtract", result.get("method").getAsString());
        assertEquals(1, result.get("id").getAsInt());
    }

    @Test
    public void testEncode_validJsonObject() {
        // Given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jsonrpc", "2.0");
        jsonObject.addProperty("method", "subtract");
        jsonObject.addProperty("id", 1);

        // When
        byte[] resultBytes = codec.encode(jsonObject);
        String resultString = new String(resultBytes, java.nio.charset.StandardCharsets.UTF_8);

        // Then
        int headerEndIndex = resultString.indexOf("\r\n\r\n");
        assertTrue(headerEndIndex > 0, "Header not found");

        String header = resultString.substring(0, headerEndIndex);
        String body = resultString.substring(headerEndIndex + 4);

        // Check Content-Length header
        int contentLengthValue = Integer.parseInt(header.replace("Content-Length: ", "").trim());
        assertEquals(body.getBytes(java.nio.charset.StandardCharsets.UTF_8).length, contentLengthValue);

        // Check body correctness
        JsonObject parsedBody = JsonParser.parseString(body).getAsJsonObject();
        assertEquals(jsonObject, parsedBody);
    }
}
