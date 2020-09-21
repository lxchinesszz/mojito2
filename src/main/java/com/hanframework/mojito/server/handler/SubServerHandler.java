package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;

/**
 * @author liuxin
 * 2020-09-02 18:11
 */
public interface SubServerHandler<T, R> {
    /**
     * 根据请求找到签名信息根据签名处理结果
     *
     * @param request 请求信息
     * @param channel 连接
     * @return RpcResponse
     */
    R handler(EnhanceChannel channel, T request) throws RemotingException;
}
