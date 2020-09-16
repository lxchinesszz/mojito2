package com.hanframework.mojito.server.impl;

import com.hanframework.mojito.handler.NettySharableExchangeServerHandler;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.http.*;
import com.hanframework.mojito.protocol.https.HttpsProtocol;
import com.hanframework.mojito.server.handler.HeartBeatRespHandler;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLEngine;
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
        if (isServer) {
            if (protocol instanceof HttpsProtocol) {
                SslContext context = ((HttpsProtocol) protocol).getContext();
                SSLEngine sslEngine = context.newEngine(socketChannel.alloc());
                sslEngine.setUseClientMode(!isServer);
                //单项认证
                sslEngine.setNeedClientAuth(false);
                cp.addFirst("ssl", new SslHandler(sslEngine));
                cp.addLast(new HttpRequestDecoderWrapper());
                cp.addLast(new HttpResponseEncoderWrapper());
                cp.addLast(new HttpObjectAggregator(65536));
                cp.addLast("compressor", new HttpContentCompressor());
            } else if (protocol instanceof HttpProtocol) {
                cp.addLast(new HttpRequestDecoderWrapper());
                cp.addLast(new HttpResponseEncoderWrapper());
                cp.addLast(new HttpObjectAggregator(65536));
                cp.addLast("compressor", new HttpContentCompressor());
            } else {
                if (Objects.nonNull(protocol.getRequestDecoder())) {
                    cp.addLast(protocol.name() + "-decoder", protocol.getRequestDecoder());
                }
                if (Objects.nonNull(protocol.getResponseEncoder())) {
                    cp.addLast(protocol.name() + "-encoder", protocol.getResponseEncoder());
                }
            }
            cp.addLast(new NettySharableExchangeServerHandler(protocol.getExchangeChannelHandler().serverChannelHandler()));
            cp.addLast(new ChannelOutboundHandlerAdapter() {
                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    System.out.println("执行写:" + msg);
                    super.write(ctx, msg, promise);
                }
            });
        } else {
            if (protocol instanceof HttpsProtocol) {
                SslContext context = ((HttpsProtocol) protocol).getContext();
                SSLEngine sslEngine = context.newEngine(socketChannel.alloc());
                sslEngine.setUseClientMode(!isServer);
                //单项认证
                sslEngine.setNeedClientAuth(false);
                cp.addFirst("ssl", new SslHandler(sslEngine));
                cp.addLast(new HttpRequestEncoderWrapper());
                cp.addLast(new HttpResponseDecoderWrapper());
                cp.addLast(new HttpObjectAggregator(65536));
                cp.addLast("compressor", new HttpContentCompressor());
            } else if (protocol instanceof HttpProtocol) {
                cp.addLast(new HttpRequestEncoderWrapper());
                cp.addLast(new HttpResponseDecoderWrapper());
                cp.addLast(new HttpObjectAggregator(65536));
                cp.addLast("compressor", new HttpContentCompressor());
            } else {
                //对于mojito协议,因为模型都继承RpcProtocolHeader所以无论客户端还是服务端的解码和编码器都是公用的
                if (Objects.nonNull(protocol.getRequestDecoder())) {
                    cp.addLast(protocol.name() + "-decoder", protocol.getRequestDecoder());
                }
                if (Objects.nonNull(protocol.getResponseEncoder())) {
                    cp.addLast(protocol.name() + "-encoder", protocol.getResponseEncoder());
                }
            }
            cp.addLast(new NettySharableExchangeServerHandler(protocol.getExchangeChannelHandler().clientChannelHandler()));
        }
    }
}
