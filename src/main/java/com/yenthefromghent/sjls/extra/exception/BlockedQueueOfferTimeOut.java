package com.yenthefromghent.sjls.extra.exception;

/** will be thrown when we exceed the wait time in a BlockedQueue **/
public class BlockedQueueOfferTimeOut extends RuntimeException {

    public BlockedQueueOfferTimeOut(String message) {
        super(message);
    }

}
