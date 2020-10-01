package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.exception.RemotingException;

/**
 * @author liuxin
 * 2020-09-02 18:11
 */
public interface BusinessHandler<T, R> {
    /**
     * 服务端业务处理器
     *
     * @param channelContext 连接通信上下文信息
     * @param request        请求信息
     * @return R
     */
    R handler(ChannelContext channelContext, T request) throws RemotingException;
}
