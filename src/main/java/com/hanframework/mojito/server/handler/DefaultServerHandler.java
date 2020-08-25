package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.protocol.mojito.model.RpcTools;
import com.hanframework.mojito.signature.service.SignatureHodler;

import java.util.Objects;

/**
 * @author liuxin
 * 2020-08-01 20:04
 */
public class DefaultServerHandler extends AbstractServerHandler<RpcRequest, RpcResponse> {


    @Override
    public RpcResponse doHandler(EnhanceChannel channel,RpcRequest rpcRequest) throws Exception {
        return RpcTools.buildNotFoundService(rpcRequest);
    }

}
