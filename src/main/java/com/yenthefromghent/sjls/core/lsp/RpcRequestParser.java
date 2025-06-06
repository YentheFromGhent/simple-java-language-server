package com.yenthefromghent.sjls.core.lsp;

import com.yenthefromghent.sjls.core.codec.MessageCodec;
import com.yenthefromghent.sjls.core.codec.RPCMessageCodec;
import com.yenthefromghent.sjls.core.io.RpcRequestReader;
import com.yenthefromghent.sjls.core.io.StdInRpcRequestReader;
import com.yenthefromghent.sjls.debug.exception.BlockedQueueOfferTimeOut;

import java.io.IOException;
import java.util.logging.Logger;

/** class that will take incoming messages from the StdinRpcRequestReader
  * and try to add them to the RpcRequestRegistery
  **/
public class RpcRequestParser implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final RpcRequestReader rpcRequestReader;
    private final RpcRequestStorer rpcRequestStorer;
    private final MessageCodec messageCodec;

    public RpcRequestParser() {
        this.rpcRequestReader = new StdInRpcRequestReader();
        this.rpcRequestStorer = new RpcRequestStorer();
        this.messageCodec = new RPCMessageCodec();
    }

    @Override
    public void run() {
        byte[] messageBytes;
        try {
            while ((messageBytes = rpcRequestReader.nextMessage()) != null) {
                this.add(messageBytes, 0);
            }
        } catch (IOException e ) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void add(byte[] messageBytes, int attempts) {
        try {
            rpcRequestStorer.add(new RpcRequest(messageCodec.decode(messageBytes)));
        } catch (BlockedQueueOfferTimeOut e) {
            if (attempts >= 5) {
                //If we could not add the request to the queue after 5 times, the server should quit.
                LOGGER.severe(
                        "Could not add message to queue after " + attempts
                                + " attempts with error: " + e.getMessage()
                );
                //TODO Notify main thread, and let it choose to restart this process, or shutdown.
                throw new RuntimeException(e.getMessage(), e);
            }
            //could not add request to queue, tyring it again.
            LOGGER.warning("Could not add request to queue, will try again..");
            this.add(messageBytes, attempts + 1);
        }
    }
}
