package com.hanframework.mojito.handler.task;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;

/**
 * @author liuxin
 * 2020-09-14 18:12
 */
public class RpcClientHandlerTask extends AbstractHandlerTask<RpcProtocolHeader, RpcProtocolHeader> {

    public RpcClientHandlerTask(Protocol<RpcProtocolHeader, RpcProtocolHeader> protocol, EnhanceChannel enhanceChannel, RpcProtocolHeader request) {
        super(protocol, enhanceChannel, request);
    }


    @Override
    public RpcProtocolHeader doResult() {
        getProtocol().getClientPromiseHandler().received(getRequest());
        return null;
    }
}
