package com.hanframework.mojito.protocol;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.ClientHandler;
import com.hanframework.mojito.client.handler.DefaultAsyncClientHandler;
import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.handler.MojitoCoreHandler;
import com.hanframework.mojito.protocol.mojito.MojitoChannelDecoder;
import com.hanframework.mojito.protocol.mojito.MojitoChannelEncoder;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.ServerHandler;
import com.hanframework.mojito.server.impl.NettyServer;

import java.util.Objects;
import java.util.concurrent.Executor;


/**
 * 1. 自己定义数据模型和服务端和客户端的处理逻辑(数据模型只要继承RpcProtocolHeader即可)
 * 2. 编码器和解码器使用默认即可,已经实现拆包和粘包问题,所以不用担心该问题
 *
 * @author liuxin
 * 2020-08-22 13:27
 */
public abstract class AbstractCodecFactory<T extends RpcProtocolHeader, R extends RpcProtocolHeader> implements CodecFactory<T, R> {

    private Executor executor = new HanThreadPoolExecutor(new NamedThreadFactory("mojimo")).getExecutory();

    private ServerHandler<T, R> serverHandler;

    private ClientHandler<T, R> clientHandler;

    @Override
    public String name() {
        return null;
    }

    @Override
    public Protocol<T, R> getProtocol() {
        return this;
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
    public Server getServer() {
        return new NettyServer(this);
    }

    @Override
    public abstract Client<T, R> getClient();

    public ServerHandler<T, R> getServerHandler() {
        if (Objects.isNull(this.serverHandler)) {
            this.serverHandler = doServerHandler();
        }
        return this.serverHandler;
    }

    public ClientHandler<T, R> getClientHandler() {
        if (Objects.isNull(this.clientHandler)) {
             new DefaultAsyncClientHandler();
            this.clientHandler = doClientHandler();
        }
        return this.clientHandler;
    }

    public abstract ServerHandler<T, R> doServerHandler();

    public abstract ClientHandler<T, R> doClientHandler();
}
