package com.hanframework.mojito.protocol.http;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author liuxin
 * 2020-09-02 14:07
 */
public class HttpResponseFacade extends BaseHttpMessage {

    private final FullHttpResponse fullHttpResponse;

    private String originId;

    public HttpResponseFacade(FullHttpResponse fullHttpResponse) {
        this(fullHttpResponse, false);
    }

    public HttpResponseFacade(FullHttpResponse fullHttpResponse, boolean client) {
        super(fullHttpResponse, client);
        this.fullHttpResponse = fullHttpResponse;
        if (client) {
            this.setId(getOriginId());
        }
    }

    public FullHttpResponse getFullHttpResponse() {
        return fullHttpResponse;
    }


    public static HttpResponseFacade HTML(String html) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(html, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return new HttpResponseFacade(response);
    }

    public static HttpResponseFacade TEXT(String string) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(string, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return new HttpResponseFacade(response);
    }

    public static HttpResponseFacade JSON(String json) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.copiedBuffer(json, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return new HttpResponseFacade(response);
    }
}
