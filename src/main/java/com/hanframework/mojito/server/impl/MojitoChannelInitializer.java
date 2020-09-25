package com.hanframework.mojito.server.impl;

import com.hanframework.mojito.handler.ExchangeChannelHandler;
import com.hanframework.mojito.handler.NettySharableExchangeServerInboundHandler;
import com.hanframework.mojito.handler.NettySharableExchangeServerOutboundHandler;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.http.*;
import com.hanframework.mojito.protocol.https.HttpsProtocol;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLEngine;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxin
 * 2020-08-21 22:49
 */
@Slf4j
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
        log.info("开始构建ChannelContext上下文");
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast("idleStateHandler", new IdleStateHandler(5, 5, 5, TimeUnit.SECONDS));
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
            ExchangeChannelHandler serverChannelHandler = protocol.getExchangeChannelHandler().serverChannelHandler();
            cp.addLast(new NettySharableExchangeServerInboundHandler(serverChannelHandler));
            cp.addLast(new NettySharableExchangeServerOutboundHandler(serverChannelHandler));
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
            ExchangeChannelHandler clientChannelHandler = protocol.getExchangeChannelHandler().clientChannelHandler();
            cp.addLast(new NettySharableExchangeServerInboundHandler(clientChannelHandler));
            cp.addLast(new NettySharableExchangeServerOutboundHandler(clientChannelHandler));

        }
    }
}
