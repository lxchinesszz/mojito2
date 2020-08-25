package com.hanframework.mojito.client.handler;


import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;

/**
 * @author liuxin
 * 2020-08-01 20:06
 */
public class DefaultAsyncClientPromiseHandler extends AbstractAsyncClientPromiseHandler<RpcRequest, RpcResponse> {

    @Override
    public void sendBefore(RpcRequest rpcRequest) {

    }

    @Override
    public void receivedBefore(RpcResponse rpcResponse) {

    }
}
