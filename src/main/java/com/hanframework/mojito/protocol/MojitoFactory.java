package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.mojito.MojitoProtocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.handler.SubServerHandler;

/**
 * @author liuxin
 * 2020-08-22 14:44
 */
public class MojitoFactory extends AbstractFactory<RpcRequest, RpcResponse> {


    private SubServerHandler<RpcRequest, RpcResponse> subServerHandler;


    public MojitoFactory(SubServerHandler<RpcRequest, RpcResponse> subServerHandler) {
        this(new MojitoProtocol(), subServerHandler);
    }

    public MojitoFactory(Protocol<RpcRequest, RpcResponse> protocol, SubServerHandler<RpcRequest, RpcResponse> subServerHandler) {
        super(protocol, subServerHandler);
    }

}
