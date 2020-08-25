package com.hanframework.mojito.protocol;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
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

    private ClientPromiseHandler<T, R> clientPromiseHandler;

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

    @Override
    public Client<T, R> getClient(String remoteHost, int remotePort) throws Exception {
        Client<T, R> client = getClient();
        client.connect(remoteHost, remotePort);
        return client;
    }

    public ServerHandler<T, R> getServerHandler() {
        if (Objects.isNull(this.serverHandler)) {
            this.serverHandler = doServerHandler();
        }
        return this.serverHandler;
    }

    public ClientPromiseHandler<T, R> getClientPromiseHandler() {
        if (Objects.isNull(this.clientPromiseHandler)) {
            this.clientPromiseHandler = doClientHandler();
        }
        return this.clientPromiseHandler;
    }

    public void setServerHandler(ServerHandler<T, R> serverHandler) {
        this.serverHandler = serverHandler;
    }

    public void setClientPromiseHandler(ClientPromiseHandler<T, R> clientPromiseHandler) {
        this.clientPromiseHandler = clientPromiseHandler;
    }

    public abstract ServerHandler<T, R> doServerHandler();

    public abstract ClientPromiseHandler<T, R> doClientHandler();
}
