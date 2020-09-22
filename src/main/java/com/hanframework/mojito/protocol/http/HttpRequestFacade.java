package com.hanframework.mojito.protocol.http;

import com.hanframework.mojito.config.Constant;
import com.hanframework.mojito.protocol.ProtocolEnum;
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
public final class HttpRequestFacade extends BaseHttpMessage {

    private final FullHttpRequest fullHttpRequest;

    private final Map<String, String> paramMap;

    private final HttpMethod httpMethod;

    public FullHttpRequest getFullHttpRequest() {
        return fullHttpRequest;
    }

    public HttpRequestFacade(FullHttpRequest fullHttpRequest) {
        this(fullHttpRequest, false);
        addHeader(Constant.REQUEST_ID, getId());
    }

    public HttpRequestFacade(FullHttpRequest fullHttpRequest, boolean server) {
        super(fullHttpRequest, server);
        this.fullHttpRequest = fullHttpRequest;
        this.paramMap = Collections.unmodifiableMap(FullHttpRequestUtils.parseParams(fullHttpRequest));
        this.httpMethod = FullHttpRequestUtils.parseHttpMethod(fullHttpRequest);
        this.setProtocolType(ProtocolEnum.HTTP.getType());
    }

    public HttpMethod method() {
        return httpMethod;
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
        String head = headers.get(HttpHeaders.Names.CONNECTION);
        return HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(head);
    }


}
