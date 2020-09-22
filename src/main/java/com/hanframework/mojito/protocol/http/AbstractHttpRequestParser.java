package com.hanframework.mojito.protocol.http;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

/**
 * @author liuxin
 * 2020-09-22 21:48
 */
public abstract class AbstractHttpRequestParser implements HttpRequestParser {




    @Override
    public HttpContentHolder parseHttpContent(FullHttpRequest fullHttpRequest) {
        Map<String, String> headers = getHeaders(fullHttpRequest);
        String body = getBody(fullHttpRequest);
        HttpMethod method = getMethod(fullHttpRequest);
        Map<String, String> paramMap = getParamMap(fullHttpRequest);
        return new HttpContentHolder(fullHttpRequest,headers, paramMap, body, method);
    }

    public abstract Map<String, String> getHeaders(FullHttpRequest fullHttpRequest);

    public abstract Map<String, String> getParamMap(FullHttpRequest fullHttpRequest);

    public abstract HttpMethod getMethod(FullHttpRequest fullHttpRequest);

    public abstract String getBody(FullHttpRequest fullHttpRequest);

}
