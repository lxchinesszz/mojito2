package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.signature.service.SignatureManager;

/**
 * 服务端将rpc请求转换成rpc响应
 *
 * @author liuxin
 * 2020-08-01 18:50
 */
public interface ServerHandler<T, R> {

    /**
     * 用户编写的服务处理器
     *
     * @param subServerHandler 用户编写的服务处理器
     */
    void initWrapper(SubServerHandler<T, R> subServerHandler);

    /**
     * 根据请求找到签名信息根据签名处理结果
     *
     * @param rpcRequest rpc请求信息
     * @return RpcResponse
     */
    R handler(EnhanceChannel channel, T rpcRequest) throws RemotingException;
}
