package com.hanframework.mojito.server;

/**
 * 服务端接口
 * 1. 启动服务
 * 2. 关闭服务
 *
 * @author liuxin
 * 2020-07-23 23:04
 */
public interface Server extends ProtocolRegister {

    /**
     * 启动服务
     *
     * @param port 服务端口号
     */
    void start(int port);

    /**
     * 非阻塞启动
     *
     * @param port 端口
     */
    void startAsync(int port);

    /**
     * 关闭服务
     */
    void close();
}
