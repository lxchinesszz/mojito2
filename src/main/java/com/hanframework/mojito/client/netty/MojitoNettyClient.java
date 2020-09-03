package com.hanframework.mojito.client.netty;

import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;

/**
 * @author liuxin
 * 2020-08-23 00:54
 */
public class MojitoNettyClient extends AbstractNettyClient<RpcRequest, RpcResponse> {
    public MojitoNettyClient(Protocol protocol) {
        super(protocol);
    }
}
