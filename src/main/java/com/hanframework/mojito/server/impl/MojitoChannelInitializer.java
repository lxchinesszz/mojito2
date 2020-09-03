package com.hanframework.mojito.server.impl;

import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.handler.MojitoCoreHandler;
import com.hanframework.mojito.handler.NettyExchangeServerHandler;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.http.HttpProtocol;
import com.hanframework.mojito.protocol.http.HttpRequestDecoderWrapper;
import com.hanframework.mojito.protocol.http.HttpResponseEncoderWrapper;
import com.hanframework.mojito.server.handler.HeartBeatRespHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxin
 * 2020-08-21 22:49
 */
public class MojitoChannelInitializer extends ChannelInitializer<SocketChannel> {


    private Protocol protocol;

    private boolean isServer;

    public MojitoChannelInitializer(Protocol protocol) {
        this.protocol = protocol;
        this.isServer = true;
    }

    public MojitoChannelInitializer(Protocol protocol, boolean isServer) {
        this.protocol = protocol;
        this.isServer = isServer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("开始构建ChannelContext上下文");
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast("idleStateHandler", new IdleStateHandler(5, 5, 5, TimeUnit.SECONDS));
        cp.addLast(new HeartBeatRespHandler());
        if (protocol instanceof HttpProtocol) {
            cp.addLast(new HttpRequestDecoderWrapper());
            cp.addLast(new HttpResponseEncoderWrapper());
            cp.addLast(new HttpObjectAggregator(65536));
        } else {
            if (Objects.nonNull(protocol.getRequestDecoder())) {
                cp.addLast(protocol.name() + "-decoder", protocol.getRequestDecoder());
            }
            if (Objects.nonNull(protocol.getResponseEncoder())) {
                cp.addLast(protocol.name() + "-encoder", protocol.getResponseEncoder());
            }
        }
        if (isServer) {
            cp.addLast(new NettyExchangeServerHandler(protocol.getRequestHandler().serverChannelHandler()));
        } else {
            cp.addLast(new NettyExchangeServerHandler(protocol.getRequestHandler().clientChannelHandler()));
        }
    }
}
