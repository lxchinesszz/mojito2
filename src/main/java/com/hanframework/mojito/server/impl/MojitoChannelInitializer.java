package com.hanframework.mojito.server.impl;

import com.hanframework.mojito.handler.NettyExchangeServerHandler;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.server.handler.HeartBeatRespHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxin
 * 2020-08-21 22:49
 */
public class MojitoChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Protocol protocol;


    public MojitoChannelInitializer(Protocol protocol) {
        this.protocol = protocol;
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("开始构建ChannelContext上下文");
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast("idleStateHandler", new IdleStateHandler(5, 5, 5, TimeUnit.SECONDS));
        cp.addLast(new HeartBeatRespHandler());
        if (Objects.nonNull(protocol.getRequestDecoder())) {
            cp.addLast(protocol.name() + "-decoder", protocol.getRequestDecoder());
        }
        if (Objects.nonNull(protocol.getResponseEncoder())) {
            cp.addLast(protocol.name() + "-encoder", protocol.getResponseEncoder());
        }
        cp.addLast(new NettyExchangeServerHandler(protocol.getRequestHandler().server()));
    }
}
