package com.hanframework.mojito.protocol.http;

import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.util.FullHttpRequestUtils;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Collections;
import java.util.Map;

/**
 * 对请求的门面
 *
 * @author liuxin
 * 2020-09-02 14:18
 */
public final class HttpRequestFacade extends RpcProtocolHeader {

    private final FullHttpRequest fullHttpRequest;

    private final Map<String, String> paramMap;

    private final Map<String, String> headers;

    private final HttpHeaders httpHeaders;

    private final HttpMethod httpMethod;

    public HttpRequestFacade(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
        this.paramMap = Collections.unmodifiableMap(FullHttpRequestUtils.parseParams(fullHttpRequest));
        this.headers = Collections.unmodifiableMap(FullHttpRequestUtils.parseHeaders(fullHttpRequest));
        this.httpMethod = FullHttpRequestUtils.parseHttpMethod(fullHttpRequest);
        this.httpHeaders = FullHttpRequestUtils.fetchHttpHeaders(fullHttpRequest);
    }

    public HttpMethod method() {
        return httpMethod;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public String getRequestURI() {
        return fullHttpRequest.uri();
    }


    public Map<String, String> getRequestParams() {
        return paramMap;
    }

    /**
     * 获取请求值
     *
     * @param paramName 请求参数,get和post都可以
     * @return String
     */
    public String getRequestParams(String paramName) {
        return paramMap.get(paramName);
    }

    /**
     * 获取请求头
     *
     * @return Map
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 获取请求头
     *
     * @param headerName 请求头
     * @return String
     */
    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    /**
     * 是否长连接
     *
     * @return boolean
     */
    public boolean keepAlive() {
        String head = getHttpHeaders().getHead(HttpHeaders.Names.CONNECTION);
        return HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(head);
    }


}
