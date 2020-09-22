package com.hanframework.mojito.protocol;

import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.processor.Processor;
import com.hanframework.mojito.processor.RequestProcessor;
import com.hanframework.mojito.processor.ResponseProcessor;
import com.hanframework.mojito.server.handler.ServerHandler;

/**
 * ConfigurableFactory
 * @author liuxin
 * 2020-08-23 21:10
 */
public interface ConfigurableFactory<T, R> extends Processor<T,R> {

    /**
     * 服务端处理逻辑
     *
     * @param serverHandler 服务端处理逻辑
     */
    void setServerHandler(ServerHandler<T, R> serverHandler);
    /**
     * 客户端处理逻辑
     *
     * @param clientPromiseHandler 客户端处理器
     */
    void setClientPromiseHandler(ClientPromiseHandler<T, R> clientPromiseHandler);

}
