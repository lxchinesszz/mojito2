package com.hanframework.mojito.handler.task;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.handler.ServerHandler;

/**
 * @author liuxin
 * 2020-09-14 17:52
 */
public class RpcServerHandlerTask extends AbstractHandlerTask<RpcProtocolHeader, RpcProtocolHeader> {

    public RpcServerHandlerTask(ServerHandler<RpcProtocolHeader, RpcProtocolHeader> serverHandler, EnhanceChannel enhanceChannel, RpcProtocolHeader request) {
        super(serverHandler, enhanceChannel, request);
    }

    @Override
    public RpcProtocolHeader doResult() {
        return getServerHandler().handler(getEnhanceChannel(), getRequest());
    }
}

