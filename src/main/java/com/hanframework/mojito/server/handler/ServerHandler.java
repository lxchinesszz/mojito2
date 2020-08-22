package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.exception.RemotingException;
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
     * 根据请求找到签名信息根据签名处理结果
     *
     * @param rpcRequest rpc请求信息
     * @return RpcResponse
     */
    R handler(T rpcRequest) throws RemotingException;
}
