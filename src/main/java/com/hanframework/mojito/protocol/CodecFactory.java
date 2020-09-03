package com.hanframework.mojito.protocol;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.ServerHandler;


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

    /**
     * 设置服务端的处理器
     * 目的将请求信息通过处理,生成返回值
     *
     * @param serverHandler 服务端处理器
     */
    void setServerHandler(ServerHandler<T, R> serverHandler);

    /**
     * 设置客户端通知处理器
     *
     * @param clientPromiseHandler
     */
    void setClientPromiseHandler(ClientPromiseHandler<T, R> clientPromiseHandler);

}
