package com.hanframework.mojito.protocol.http;

import com.hanframework.mojito.util.FullHttpRequestUtils;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

/**
 * @author liuxin
 * 2020-09-22 21:48
 */
public abstract class AbstractHttpRequestParser implements HttpRequestParser {


    @Override
    public HttpContentHolder parseHttpContent(FullHttpRequest fullHttpRequest) {
        System.out.println("fullHttpRequest.refCnt()==" + fullHttpRequest.refCnt());
        Map<String, String> headers = getHeaders(fullHttpRequest);
        String body = getBody(fullHttpRequest);
        HttpMethod method = getMethod(fullHttpRequest);
        Map<String, String> paramMap = getParamMap(fullHttpRequest);
        return new HttpContentHolder(fullHttpRequest, headers, paramMap, body, method);
    }

    /**
     * 解析http请求头
     *
     * @param fullHttpRequest 请求信息
     * @return Map
     */
    public abstract Map<String, String> getHeaders(FullHttpRequest fullHttpRequest);

    /**
     * 解析http请求参数,key-value类型参数
     *
     * @param fullHttpRequest 请求信息
     * @return Map
     */
    public abstract Map<String, String> getParamMap(FullHttpRequest fullHttpRequest);

    /**
     * 解析http请求类型
     *
     * @param fullHttpRequest 请求信息
     * @return Map
     */
    public HttpMethod getMethod(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.parseHttpMethod(fullHttpRequest);
    }

    /**
     * 解析http放在请求体的数据
     *
     * @param fullHttpRequest 请求信息
     * @return Map
     */
    public abstract String getBody(FullHttpRequest fullHttpRequest);

}
