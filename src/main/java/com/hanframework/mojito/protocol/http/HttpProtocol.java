package com.hanframework.mojito.protocol.http;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.handler.AbstractAsyncClientPromiseHandler;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.handler.MojitoCoreHandler;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.server.handler.AbstractServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2020-07-31 18:06
 */
public class HttpProtocol implements Protocol<HttpRequestFacade, HttpResponseFacade> {

    private Executor executor = new HanThreadPoolExecutor(new NamedThreadFactory("http")).getExecutory();

    private ServerHandler<HttpRequestFacade, HttpResponseFacade> serverHandler;

    private ClientPromiseHandler<HttpRequestFacade, HttpResponseFacade> clientPromiseHandler;

    @Override
    public String name() {
        return "http";
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
        //是一种特殊的协议,不返回直接使用netty支持的协议
        return null;
    }

    @Override
    public ChannelEncoder getResponseEncoder() {
        ////是一种特殊的协议,不返回直接使用netty支持的协议
        return null;
    }

    @Override
    public ServerHandler<HttpRequestFacade, HttpResponseFacade> getServerHandler() {
        if (Objects.isNull(this.serverHandler)) {
            this.serverHandler = new AbstractServerHandler<HttpRequestFacade, HttpResponseFacade>() {
            };
        }
        return this.serverHandler;
    }

    @Override
    public ClientPromiseHandler<HttpRequestFacade, HttpResponseFacade> getClientPromiseHandler() {
        if (Objects.isNull(this.clientPromiseHandler)) {
            this.clientPromiseHandler = new AbstractAsyncClientPromiseHandler<>();
        }
        return this.clientPromiseHandler;
    }

    @Override
    public void setServerHandler(ServerHandler<HttpRequestFacade, HttpResponseFacade> serverHandler) {
        this.serverHandler = serverHandler;
    }
}
