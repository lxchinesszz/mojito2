package com.hanframework.mojito.protocol.http;

import com.google.common.base.Strings;
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
        super(fullHttpResponse,client);
        this.fullHttpResponse = fullHttpResponse;
        if (client) {
            this.setId(getOriginId());
        }
    }

    public FullHttpResponse getFullHttpResponse() {
        return fullHttpResponse;
    }


    public static HttpResponseFacade ok(String i) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer("<html><body><h1>Hello Mojito:" + i + "</h1></body></html>", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return new HttpResponseFacade(response);
    }

    public static HttpResponseFacade ok() {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer("<html><body><h1>Hello Mojito</h1></body></html>", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return new HttpResponseFacade(response);
    }

    public static HttpResponseFacade JSON(int i) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.copiedBuffer("{'name':'lx','age':" + i + "}", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return new HttpResponseFacade(response);
    }
}
