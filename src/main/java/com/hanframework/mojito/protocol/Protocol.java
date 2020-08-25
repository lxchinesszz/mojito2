package com.hanframework.mojito.protocol;

import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.util.concurrent.Executor;

/**
 * 协议的组成部分
 * 协议解码器 + 协议处理器 + 协议处理器
 *
 * @author liuxin
 * 2020-07-25 21:39
 */
public interface Protocol<R, V> {

    /**
     * 协议名称
     *
     * @return String
     */
    String name();

    /**
     * 连接处理器
     * 面向连接编程,处理客户端和服务端的交互。
     */
    MojitoChannelHandler getRequestHandler();

    /**
     * 可以指定处理器,如果不指定默认使用netty work线程
     *
     * @return Executor
     */
    Executor getExecutor();

    /**
     * 请求解码器
     *
     * @return ChannelDecoder
     */
    ChannelDecoder getRequestDecoder();

    /**
     * 响应编码器
     *
     * @return ChannelEncoder
     */
    ChannelEncoder getResponseEncoder();

    /**
     * 服务处理器，负责将请求信息转换成响应信息
     * 注意: 非线程安全类
     *
     * @return ServerHandler
     */
    ServerHandler<R, V> getServerHandler();

    /**
     * 客户端处理器
     * 注意: 非线程安全类
     *
     * @return ClientPromiseHandler
     */
    ClientPromiseHandler<R, V> getClientPromiseHandler();

}
