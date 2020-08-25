package com.hanframework.mojito.protocol.mojito;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.client.handler.DefaultAsyncClientPromiseHandler;
import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.handler.MojitoCoreHandler;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.handler.DefaultServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2020-07-31 22:01
 */
public class MojitoProtocol implements Protocol<RpcRequest, RpcResponse> {

    private Executor executor = new HanThreadPoolExecutor(new NamedThreadFactory("mojimo")).getExecutory();

    private ChannelDecoder channelDecoder;

    private ChannelEncoder channelEncoder;

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
//        if (Objects.isNull(this.channelDecoder)) {
//            this.channelDecoder = new MojitoChannelDecoder("server");
//        }
        return new MojitoChannelDecoder("server");
    }

    @Override
    public ChannelEncoder getResponseEncoder() {
//        if (Objects.isNull(this.channelEncoder)) {
//            this.channelEncoder = new MojitoChannelEncoder("server");
//        }
        //每个链接不能共享一个。如果共享一个可能会出现两个链接的数据写到一起。
        return new MojitoChannelEncoder("server");
    }

    @Override
    public ServerHandler<RpcRequest, RpcResponse> getServerHandler() {
        if (Objects.isNull(this.serverHandler)) {
            this.serverHandler = new DefaultServerHandler();
        }
        return this.serverHandler;
    }

    @Override
    public ClientPromiseHandler<RpcRequest, RpcResponse> getClientPromiseHandler() {
        if (Objects.isNull(this.clientPromiseHandler)) {
            this.clientPromiseHandler = new DefaultAsyncClientPromiseHandler();
        }
        return this.clientPromiseHandler;
    }
}

