package com.yenthefromghent.sjls.core.lsp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * anotation to define that the method is callable using jsrpc
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RPCMethod {}
