package com.hanframework.mojito.protocol.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.util.Iterator;
import java.util.List;

/**
 * http客户端使用,对服务端的响应解码
 *
 * @author liuxin
 * 2020-09-02 14:33
 */
public class HttpResponseDecoderWrapper extends HttpResponseDecoder {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        super.decode(ctx, buffer, out);
    }
}
