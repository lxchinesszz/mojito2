package com.hanframework.mojito.protocol.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequestEncoder;

import java.util.List;

/**
 * @author liuxin
 * 2020-09-02 14:33
 */
public class HttpRequestEncoderWrapper extends HttpRequestEncoder {

    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return msg instanceof HttpRequestFacade;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if (msg instanceof HttpRequestFacade) {
            msg = ((HttpRequestFacade) msg).getFullHttpRequest();
        }
        super.encode(ctx, msg, out);
    }

}
