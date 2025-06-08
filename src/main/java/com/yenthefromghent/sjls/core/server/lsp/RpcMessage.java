package com.yenthefromghent.sjls.core.server.lsp;

import com.google.gson.JsonObject;

public record RpcMessage(JsonObject content, RequestType type) {}
