package com.yenthefromghent.sjls.core.server.lsp;

import com.yenthefromghent.sjls.extra.exception.BlockedQueueOfferTimeOut;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RpcMessageStorer {

    private final Logger LOGGER = Logger.getLogger("main");
    private final Logger READER_LOGGER = Logger.getLogger("reader");

    private final BlockingQueue<RpcMessage> rpcMessages = new LinkedBlockingQueue<>();

    public RpcMessageStorer() {
        LOGGER.finest("initializng RpcRequestStorer");
    }

    public void add(RpcMessage rpcMessage) throws BlockedQueueOfferTimeOut {
        try {
            boolean succes =  rpcMessages.offer(rpcMessage, 200, TimeUnit.MILLISECONDS);
            if (!succes) {
                READER_LOGGER.warning("RPC content timed out");
                throw new BlockedQueueOfferTimeOut("blocked queue offering timeout exception");
            }
            READER_LOGGER.finest("message stored");
        } catch (InterruptedException e) {
            throw new BlockedQueueOfferTimeOut("blocked queue offering timeout exception");
        }
    }

    public RpcMessage get() throws InterruptedException {
        LOGGER.finest("attempting to take message");
        return rpcMessages.take();
    }

}
