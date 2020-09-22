package com.hanframework.mojito.protocol.http;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

/**
 * @author liuxin
 * 2020-09-22 21:51
 */
public class HttpContentHolder {

    protected FullHttpRequest fullHttpRequest;
    /**
     * 请求头或者响应头
     */
    protected final Map<String, String> headers;

    private final Map<String, String> paramMap;

    private final String body;

    private final HttpMethod httpMethod;

    public HttpContentHolder(FullHttpRequest fullHttpRequest, Map<String, String> headers, Map<String, String> paramMap, String body, HttpMethod httpMethod) {
        this.fullHttpRequest = fullHttpRequest;
        this.headers = headers;
        this.paramMap = paramMap;
        this.body = body;
        this.httpMethod = httpMethod;
    }

    public FullHttpRequest getFullHttpRequest() {
        return fullHttpRequest;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
