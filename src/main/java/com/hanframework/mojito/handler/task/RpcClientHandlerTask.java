package com.hanframework.mojito.handler.task;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.protocol.Protocol;

/**
 * @author liuxin
 * 2020-09-14 18:12
 */
public class RpcClientHandlerTask extends AbstractHandlerTask<Object, Object> {

    public RpcClientHandlerTask(Protocol<Object, Object> protocol, EnhanceChannel enhanceChannel, Object request) {
        super(protocol, enhanceChannel, request);
    }


    @Override
    public Void doResult() {
        getProtocol().getClientPromiseHandler().received(getRequest());
        return null;
    }
}
