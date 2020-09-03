package com.hanframework.mojito.protocol.mojito;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.client.handler.DefaultAbstractAsyncClientPromiseHandler;
import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.handler.MojitoCoreHandler;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.handler.MojitoServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2020-07-31 22:01
 */
public class MojitoProtocol implements Protocol<RpcRequest, RpcResponse> {

    private Executor executor = new HanThreadPoolExecutor(new NamedThreadFactory("mojimo")).getExecutory();

    private ServerHandler<RpcRequest, RpcResponse> serverHandler;

    private ClientPromiseHandler<RpcRequest, RpcResponse> clientPromiseHandler;

    @Override
    public String name() {
        return "mojito";
    }

    @Override
    public MojitoChannelHandler getRequestHandler() {
        return new MojitoCoreHandler(this);
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public ChannelDecoder getRequestDecoder() {
        return new MojitoChannelDecoder("MojitoChannelDecoder");
    }

    @Override
    public ChannelEncoder getResponseEncoder() {
        return new MojitoChannelEncoder("MojitoChannelEncoder");
    }

    @Override
    public ServerHandler<RpcRequest, RpcResponse> getServerHandler() {
        if (Objects.isNull(this.serverHandler)) {
            this.serverHandler = new MojitoServerHandler();
        }
        return this.serverHandler;
    }

    @Override
    public ClientPromiseHandler<RpcRequest, RpcResponse> getClientPromiseHandler() {
        if (Objects.isNull(this.clientPromiseHandler)) {
            this.clientPromiseHandler = new DefaultAbstractAsyncClientPromiseHandler();
        }
        return this.clientPromiseHandler;
    }

    @Override
    public void setServerHandler(ServerHandler<RpcRequest, RpcResponse> serverHandler) {
        this.serverHandler = serverHandler;
    }
}

