package com.hanframework.mojito.protocol.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequestDecoder;

import java.util.List;

/**
 * @author liuxin
 * 2020-09-02 14:33
 */
public class HttpRequestDecoderWrapper extends HttpRequestDecoder {

    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        super.decode(ctx, buffer, out);
    }
}
