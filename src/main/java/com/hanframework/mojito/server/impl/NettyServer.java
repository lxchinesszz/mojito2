package com.hanframework.mojito.server.impl;

import com.hanframework.kit.platform.OSinfo;
import com.hanframework.kit.text.Ansi;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.banner.Banner;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.server.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuxin
 * 2020-07-25 21:11
 */
@Slf4j
public class NettyServer extends AbstractServer {

    private Channel channel;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));

    /**
     * 每个CPU同一时间片只能处理一个线程
     * - 对于IO密集型(当任何一个线程在进行IO操作时候,线程都会处于阻塞状态,则处理器可以立即进行上下文切换让其他线程作业)
     * ,对cpu要求不高所以数量可以大于CPU核心数,建议2N(CPU核心数)
     * <p>
     * - 计算密集型(当任何一个线程发生任何暂定或错误,刚好有一个额外线程能补充上,不浪费CPU使用率)
     * 对CPU要求高的,线程数量 N(CPU核心数) + 1
     */
    private static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);


    private Protocol protocol;

    public NettyServer(Protocol protocol) {
        this.protocol = protocol;
    }

    private void createServer(int port, boolean async) {
        bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory("mojito-boss", true));
        workerGroup = new NioEventLoopGroup(DEFAULT_IO_THREADS, new NamedThreadFactory("mojito-work", true));
        serverBootstrap.group(bossGroup, workerGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 128)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(OSinfo.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .localAddress(port).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new MojitoChannelInitializer(protocol));
        try {
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    Banner.print();
                    log.info(Banner.print("麻烦给我的爱人来一杯Mojito,我喜欢阅读她微醺时的眼眸！", Ansi.Color.RED));
                    log.info("Mojito启动成功,端口号:" + port);
                }
            });
            channel = sync.channel();
            //如果不是异步就阻塞
            if (!async) {
                channel.closeFuture().sync();
                log.info("阻塞服务启动成功");
            } else {
                log.info("异步服务启动成功");
            }
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            e.printStackTrace();
        }
        //bind
    }

    public void doOpen(int port, boolean async) {
        super.setPort(port);
        super.setAsync(async);
        createServer(port, async);
    }

    protected void doClose() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        bossGroup = null;
        workerGroup = null;
        channel = null;
        serverBootstrap = null;
    }

    @Override
    public void registerProtocol(Protocol protocol) {
        this.protocol = protocol;
        doClose();
        createServer(getPort(), isAsync());
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }
}
