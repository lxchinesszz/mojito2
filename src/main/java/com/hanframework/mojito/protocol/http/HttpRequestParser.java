package com.hanframework.mojito.protocol.http;


import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author liuxin
 * 2020-09-22 21:45
 */
public interface HttpRequestParser {

    HttpContentHolder parseHttpContent(FullHttpRequest fullHttpRequest);
}
