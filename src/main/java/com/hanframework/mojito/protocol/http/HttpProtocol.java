package com.hanframework.mojito.protocol.http;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.handler.AsyncClientPromiseHandler;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.handler.ExchangeChannelHandler;
import com.hanframework.mojito.handler.SingletonExchangeChannelHandler;
import com.hanframework.mojito.protocol.AbstractProtocol;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.server.handler.BusinessHandler;
import com.hanframework.mojito.server.handler.DefaultServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2020-07-31 18:06
 */
public class HttpProtocol extends AbstractProtocol<HttpRequestFacade, HttpResponseFacade> {

    private Executor executor = new HanThreadPoolExecutor(new NamedThreadFactory("http")).getExecutory();

    public HttpProtocol() {
        super("http");
    }

    public HttpProtocol(BusinessHandler<HttpRequestFacade, HttpResponseFacade> businessHandler) {
        super("http", businessHandler);
    }

    public HttpProtocol(String name, BusinessHandler<HttpRequestFacade, HttpResponseFacade> businessHandler) {
        super(name, businessHandler);
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
}
