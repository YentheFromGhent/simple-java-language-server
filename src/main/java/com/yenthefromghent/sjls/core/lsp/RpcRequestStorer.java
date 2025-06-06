package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.extra.exception.BlockedQueueOfferTimeOut;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RpcRequestStorer {

    private final Logger LOGGER = Logger.getLogger("main");

    private final BlockingQueue<RpcRequest> rpcRequests = new LinkedBlockingQueue<>();

    public void add(RpcRequest rpcRequest) throws BlockedQueueOfferTimeOut {
        try {
            boolean succes =  rpcRequests.offer(rpcRequest, 200, TimeUnit.MILLISECONDS);
            if (!succes) {
                LOGGER.warning("RPC request timed out");
                throw new BlockedQueueOfferTimeOut("blocked queue offering timeout exception");
            }
        } catch (InterruptedException e) {
            throw new BlockedQueueOfferTimeOut("blocked queue offering timeout exception");
        }
    }

    public RpcRequest get() throws InterruptedException {
        return rpcRequests.take();
    }

}
