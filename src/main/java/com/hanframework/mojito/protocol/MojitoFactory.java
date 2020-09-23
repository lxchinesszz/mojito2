package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.mojito.MojitoProtocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.handler.BusinessHandler;

/**
 * @author liuxin
 * 2020-08-22 14:44
 */
public class MojitoFactory extends AbstractFactory<RpcRequest, RpcResponse> {


    public MojitoFactory(BusinessHandler<RpcRequest, RpcResponse> businessHandler) {
        this(new MojitoProtocol(businessHandler), businessHandler);
    }

    public MojitoFactory(Protocol<RpcRequest, RpcResponse> protocol, BusinessHandler<RpcRequest, RpcResponse> businessHandler) {
        super(protocol, businessHandler);
    }

}
