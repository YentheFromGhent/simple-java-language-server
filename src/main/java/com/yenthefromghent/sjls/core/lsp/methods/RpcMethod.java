package com.yenthefromghent.sjls.core.lsp.methods;

import com.google.gson.JsonObject;

/**
 * classes that implement this would be rpc-request methods.
 * each class should contain the implementation of this method
 */
public interface RpcMethod {

    //We give the request with it, so that we can extract the params, and id
    void invoke(JsonObject request);

}