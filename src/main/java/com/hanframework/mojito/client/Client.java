package com.hanframework.mojito.client;

import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;

/**
 * 客户端接口
 * 1. 连接远程服务
 * 2. 发送数据
 *
 * @author liuxin
 * 2020-07-23 23:04
 */
public interface Client<T extends RpcProtocolHeader,R extends RpcProtocolHeader> {

    /**
     * 连接远程服务
     *
     * @param remoteHost 远程地址
     * @param remotePort 远程端口
     */
    void connect(String remoteHost, int remotePort) throws Exception;

    /**
     * 发送数据
     * 数据的解析和数据的编码要用指定的类处理
     *
     * @param message 发送的数据
     */
    MojitoFuture<R> sendAsync(T message) throws Exception;

    /**
     * 关闭连接
     */
    void close();
}
