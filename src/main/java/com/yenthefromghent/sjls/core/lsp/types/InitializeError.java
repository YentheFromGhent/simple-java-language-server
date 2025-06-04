package com.yenthefromghent.sjls.core.lsp.types;

import com.yenthefromghent.sjls.core.lsp.error.AbstractError;

public class InitializeError extends AbstractError {

    /**
     * type that defines the intitialization error.
     * if true: client will retry connection
     */
    public boolean retry;

    public InitializeError(boolean retry) {
        this.retry = retry;
    }

}
