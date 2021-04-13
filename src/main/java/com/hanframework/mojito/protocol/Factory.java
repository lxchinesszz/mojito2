package com.hanframework.mojito.protocol;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.BusinessHandler;
import com.hanframework.mojito.server.handler.ServerHandler;


/**
 * @author liuxin
 * 2020-08-22 14:32
 */
public interface Factory<T extends RpcProtocolHeader, R extends RpcProtocolHeader> extends ConfigurableFactory<T, R>, Protocol<T, R> {

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
     * @param remoteHost 连接ip
     * @param remotePort 连接端口
     * @return Client
     * @throws Exception 未知异常
     */
    Client<T, R> getClient(String remoteHost, int remotePort) throws Exception;

    /**
     * 设置服务端的处理器
     * 目的将请求信息通过处理,生成返回值
     *
     * @param businessHandler 服务端处理器
     */
    @Override
    void setBusinessHandler(BusinessHandler<T, R> businessHandler);

    /**
     * 设置客户端通知处理器
     * 通道是长连接,该处理器目的是保证在长连接下,数据的一一对应关系
     *
     * @param clientPromiseHandler 客户处理器
     */
    @Override
    void setClientPromiseHandler(ClientPromiseHandler<T, R> clientPromiseHandler);

}
