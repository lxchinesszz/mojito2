package com.hanframework.mojito.client.netty;

import com.hanframework.mojito.channel.DefaultEnhanceChannel;
import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.HeartBeatReqHandler;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.handler.NettyExchangeServerHandler;
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

/**
 * @author liuxin
 * 2020-08-01 17:17
 */
@Slf4j
public abstract class AbstractNettyClient<T extends RpcProtocolHeader, R extends RpcProtocolHeader> implements Client<T, R> {


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


    public AbstractNettyClient(Protocol protocol) {
        this.protocol = protocol;
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
            clientBootstrap.handler(new MojitoChannelInitializer(protocol,false));
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

    private void checked() throws ConnectException {
        if (enhanceChannel == null || !enhanceChannel.isConnected()) {
            throw new ConnectException("未连接请先检查连接");
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
            } else {
                //TODO 关闭状态
            }
        } finally {
            enhanceChannel = null;
        }
    }
}
