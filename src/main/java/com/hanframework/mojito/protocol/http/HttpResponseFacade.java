package com.hanframework.mojito.protocol.http;

import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author liuxin
 * 2020-09-02 14:07
 */
public class HttpResponseFacade extends RpcProtocolHeader {

    private final FullHttpResponse fullHttpResponse;

    public HttpResponseFacade(FullHttpResponse fullHttpResponse) {
        this.fullHttpResponse = fullHttpResponse;
    }

    public FullHttpResponse getFullHttpResponse() {
        return fullHttpResponse;
    }

    public static HttpResponseFacade ok() {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer("<html><body><h1>Hello Mojito</h1></body></html>", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        return new HttpResponseFacade(response);
    }

    public static HttpResponseFacade JSON() {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        return new HttpResponseFacade(response);
    }
}
