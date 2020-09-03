package com.hanframework.mojito.protocol.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.List;

/**
 * @author liuxin
 * 2020-09-02 15:12
 */
public class HttpResponseEncoderWrapper extends HttpResponseEncoder {

    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return msg instanceof HttpResponseFacade;
    }

    public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if (msg instanceof HttpResponseFacade) {
            msg = ((HttpResponseFacade) msg).getFullHttpResponse();
        }
        super.encode(ctx, msg, out);
    }

}
