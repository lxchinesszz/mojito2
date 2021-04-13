package com.hanframework.mojito.client.netty;

import com.hanframework.mojito.channel.DefaultEnhanceChannel;
import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.CountRetryImpl;
import com.hanframework.mojito.client.Retry;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.impl.MojitoChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxin
 * 2020-08-01 17:17
 */
@Slf4j
public class DefaultNettyClient<T extends RpcProtocolHeader, R extends RpcProtocolHeader> implements Client<T, R> {


    private Lock connLock = new ReentrantLock();

    /**
     * 当前通讯的channel
     */
    private EnhanceChannel enhanceChannel;

    /**
     * 当前客户端开关状态
     */
    private AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * 线程组
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 支持的协议信息
     */
    private Protocol protocol;

    /**
     * 将要连接的远程地址
     */
    private String remoteHost;

    /**
     * 将要连接的远程端口
     */
    private int remotePort;

    /**
     * 断线重试
     * 默认不重试
     */
    private Retry boltRetry;

    public DefaultNettyClient(Protocol protocol) {
        this(protocol, new CountRetryImpl(0));
    }

    public DefaultNettyClient(Protocol protocol, Retry boltRetry) {
        this.protocol = protocol;
        this.boltRetry = boltRetry;
    }

    private String getRemoteHost() {
        return remoteHost;
    }

    private void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    private int getRemotePort() {
        return remotePort;
    }

    private void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    /**
     * 连接远程服务
     *
     * @param remoteHost 远程地址
     * @param remotePort 远程端口
     */
    @Override
    public void connect(String remoteHost, int remotePort) throws Exception {
        try {
            setRemoteHost(remoteHost);
            setRemotePort(remotePort);
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(workerGroup);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.option(ChannelOption.TCP_NODELAY, false);
            clientBootstrap.handler(new MojitoChannelInitializer(protocol, false));
            ChannelFuture channelFuture = clientBootstrap.connect(getRemoteHost(), getRemotePort()).sync();
            enhanceChannel = DefaultEnhanceChannel.getOrAddChannel(channelFuture.channel());
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                throw new ConnectException("请检查连接信息," + e.getMessage());
            }
            throw e;
        }
    }

    public EnhanceChannel getChannel() {
        return this.enhanceChannel;
    }

    private void retryConnection() throws ConnectException {
        connLock.lock();
        try {
            while (boltRetry.canRetryIncrease()) {
                try {
                    this.connect(remoteHost, remotePort);
                } catch (Exception e) {
                    log.error("重试失败" + e.getMessage());
                }
                if (enhanceChannel.isConnected()) {
                    //重试成功就重置
                    boltRetry.reset();
                    return;
                }
            }
            //重试结束发现还是不可以连接提示
            if (enhanceChannel.isConnected()) {
                throw new ConnectException("重试连接失败,重试次数:" + boltRetry.retryCount());
            }
        } finally {
            connLock.unlock();
        }
    }

    private void checked() throws ConnectException {
        if ((enhanceChannel == null || !enhanceChannel.isConnected()) && !boltRetry.canRetry()) {
            throw new ConnectException("未连接请先检查连接");
        } else if (boltRetry.canRetry()) {
            //重试
            retryConnection();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public MojitoFuture<R> sendAsync(T message) throws Exception {
        checked();
        return protocol.getClientPromiseHandler().sendAsync(enhanceChannel, message);
    }


    @Override
    public void close() {
        try {
            if (!closed.get()) {
                closed.compareAndSet(false, true);
                workerGroup.shutdownGracefully();
                enhanceChannel.disconnected();
                log.info("Client 关闭成功");
            } else {
                //TODO 关闭状态
            }
        } finally {
            enhanceChannel = null;
        }
    }
}
