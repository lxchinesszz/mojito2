package com.hanframework.mojito.client.netty;

import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;

/**
 * @author liuxin
 * 2020-08-23 00:54
 */
public class DefaultNettyClient extends AbstractNettyClient<RpcRequest, RpcResponse> {
    public DefaultNettyClient(Protocol protocol) {
        super(protocol);
    }
}
