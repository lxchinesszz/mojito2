package com.hanframework.mojito.protocol;

import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

/**
 * @author liuxin
 * 2020-08-23 21:10
 */
public interface ConfigurableCodecFactory<T,R> {


    void setServerHandler(ServerHandler<T, R> serverHandler);

    void setClientPromiseHandler(ClientPromiseHandler<T, R> clientPromiseHandler);
}
