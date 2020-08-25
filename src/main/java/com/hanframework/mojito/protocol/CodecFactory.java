package com.hanframework.mojito.protocol;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.net.ConnectException;

/**
 * @author liuxin
 * 2020-08-22 14:32
 */
public interface CodecFactory<T extends RpcProtocolHeader, R extends RpcProtocolHeader> extends ConfigurableCodecFactory<T, R>, Protocol<T, R> {

    /**
     * 通信的协议信息
     *
     * @return Protocol
     */
    Protocol<T, R> getProtocol();

    /**
     * 服务端实例
     *
     * @return Server
     */
    Server getServer();

    /**
     * 客户端实例
     *
     * @return Client
     */
    Client<T, R> getClient();

    /**
     * 客户端实例
     *
     * @return Client
     */
    Client<T, R> getClient(String remoteHost, int remotePort) throws Exception;


    void setServerHandler(ServerHandler<T, R> serverHandler);

    void setClientPromiseHandler(ClientPromiseHandler<T, R> clientPromiseHandler);

}
