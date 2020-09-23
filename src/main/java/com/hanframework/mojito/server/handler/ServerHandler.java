package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;

/**
 * 服务端将rpc请求转换成rpc响应
 *
 * @author liuxin
 * 2020-08-01 18:50
 */
public interface ServerHandler<T extends RpcProtocolHeader, R extends RpcProtocolHeader> {

    /**
     * 用户编写的服务处理器
     *
     * @param businessHandler 用户编写的服务处理器
     */
    void initWrapper(BusinessHandler<T, R> businessHandler);

    /**
     * 是否已经初始化
     * @return boolean
     */
    boolean inited();

    /**
     * 根据请求找到签名信息根据签名处理结果
     *
     * @param channel    连接
     * @param rpcRequest rpc请求信息
     * @return RpcResponse
     */
    R handler(EnhanceChannel channel, T rpcRequest) throws RemotingException;
}
