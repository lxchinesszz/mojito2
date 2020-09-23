package com.hanframework.mojito.protocol;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.handler.AsyncClientPromiseHandler;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.handler.ExchangeChannelHandler;
import com.hanframework.mojito.handler.SingletonExchangeChannelHandler;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.handler.BusinessHandler;
import com.hanframework.mojito.server.handler.DefaultServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2020-09-23 20:40
 */
public abstract class AbstractProtocol<R extends RpcProtocolHeader, V extends RpcProtocolHeader> implements Protocol<R, V> {

    /**
     * 模型线程执行
     */
    private Executor executor = new HanThreadPoolExecutor(new NamedThreadFactory("mojimo")).getExecutory();

    /**
     * 协议名
     */
    private String name;

    /**
     * 框架对用户的业务处理器进行处理的处理器
     */
    private ServerHandler<R, V> serverHandler = new DefaultServerHandler<>();

    /**
     * 用户要编写的业务处理器
     */
    private BusinessHandler<R, V> businessHandler;

    /**
     * 客户端处理器
     */
    private ClientPromiseHandler<R, V> clientPromiseHandler;

    public AbstractProtocol(String name) {
        this.name = name;
    }

    public AbstractProtocol(String name, BusinessHandler<R, V> businessHandler) {
        this.businessHandler = businessHandler;
        this.name = name;
        installBusinessHandler(businessHandler);
    }

    private void installBusinessHandler(BusinessHandler<R, V> businessHandler) {
        this.serverHandler.initWrapper(businessHandler);
    }

    @Override
    public String name() {
        return name;
    }

    /**
     * NettyAPI和Mojito框架API的粘合剂
     *
     * @return ExchangeChannelHandler
     */
    @Override
    public ExchangeChannelHandler getExchangeChannelHandler() {
        return new SingletonExchangeChannelHandler(this);
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public abstract ChannelDecoder getRequestDecoder();

    @Override
    public abstract ChannelEncoder getResponseEncoder();

    @Override
    public ServerHandler<R, V> getServerHandler() {
        if (Objects.isNull(serverHandler)) {
            serverHandler = new DefaultServerHandler<>();
        }
        if (serverHandler.inited()) {
            if (this.businessHandler != null) {
                serverHandler.initWrapper(businessHandler);
            }
        }
        return serverHandler;
    }

    @Override
    public void setBusinessHandler(BusinessHandler<R, V> businessHandler) {
        this.businessHandler = businessHandler;
        installBusinessHandler(businessHandler);
    }

    @Override
    public ClientPromiseHandler<R, V> getClientPromiseHandler() {
        if (Objects.isNull(this.clientPromiseHandler)) {
            this.clientPromiseHandler = new AsyncClientPromiseHandler<>();
        }
        return this.clientPromiseHandler;
    }
}
