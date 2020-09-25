package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;

/**
 * @author liuxin
 * 2020-09-02 18:11
 */
public interface BusinessHandler<T, R> {
    /**
     * 服务端业务处理器
     *
     * @param channel 连接通信
     * @param request 请求信息
     * @return R
     */
    R handler(EnhanceChannel channel, T request) throws RemotingException;
}
