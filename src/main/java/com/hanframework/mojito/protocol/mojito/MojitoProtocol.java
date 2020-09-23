package com.hanframework.mojito.protocol.mojito;


import com.hanframework.mojito.protocol.AbstractProtocol;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.handler.BusinessHandler;

/**
 * 系统默认的数据模型
 * RpcRequest
 * RpcResponse
 *
 * @author liuxin
 * 2020-07-31 22:01
 */
public class MojitoProtocol extends AbstractProtocol<RpcRequest, RpcResponse> {

    public MojitoProtocol(BusinessHandler<RpcRequest, RpcResponse> businessHandler) {
        super("mojito", businessHandler);
    }

    @Override
    public ChannelDecoder getRequestDecoder() {
        return new MojitoChannelDecoder("MojitoChannelDecoder");
    }

    @Override
    public ChannelEncoder getResponseEncoder() {
        return new MojitoChannelEncoder("MojitoChannelEncoder");
    }

}

