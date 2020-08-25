package com.hanframework.mojito.protocol;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.client.handler.DefaultAsyncClientPromiseHandler;
import com.hanframework.mojito.client.netty.DefaultNettyClient;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.DefaultServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;
import com.hanframework.mojito.server.impl.NettyServer;

/**
 * @author liuxin
 * 2020-08-22 14:44
 */
public class MojitoCodecFactory extends AbstractCodecFactory<RpcRequest, RpcResponse> {


    @Override
    public ServerHandler<RpcRequest, RpcResponse> doServerHandler() {
        return new DefaultServerHandler();
    }

    @Override
    public ClientPromiseHandler<RpcRequest, RpcResponse> doClientHandler() {
        return new DefaultAsyncClientPromiseHandler();
    }

    @Override
    public Server getServer() {
        return new NettyServer(this);
    }

    @Override
    public Client<RpcRequest, RpcResponse> getClient() {
        return new DefaultNettyClient(this);
    }
}
