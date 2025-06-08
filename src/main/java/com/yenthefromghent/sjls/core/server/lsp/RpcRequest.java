package com.yenthefromghent.sjls.core.server.lsp;

public record RpcRequest(String method, int id, Object[] args) {
}
