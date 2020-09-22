package com.hanframework.mojito.util;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxin
 * 2020-09-04 21:30
 */
public final class FullHttpRequestUtils {

    public static com.hanframework.mojito.protocol.http.HttpMethod parseHttpMethod(HttpRequest httpRequest) {
        HttpMethod method = httpRequest.method();
        return com.hanframework.mojito.protocol.http.HttpMethod.ofByName(method.name());
    }

    public static com.hanframework.mojito.protocol.http.HttpHeaders fetchHttpHeaders(FullHttpRequest fullHttpRequest) {
        Map<String, String> parseHeaders = parseHeaders(fullHttpRequest);
        return new com.hanframework.mojito.protocol.http.HttpHeaders(parseHeaders);
    }

    public static Map<String, String> parseHeaders(HttpMessage httpMessage) {
        if (httpMessage == null) {
            return Collections.emptyMap();
        }
        HttpHeaders headers = httpMessage.headers();
        Map<String, String> headerMap = new HashMap<>(headers.size());
        for (Map.Entry<String, String> entry : headers.entries()) {
            headerMap.put(entry.getKey(), entry.getValue());
        }
        return headerMap;
    }

    public static String fetchBody(FullHttpRequest fullHttpRequest) {
        return ByteBufUtils.toString(fullHttpRequest.content());
    }

    public static Map<String, String> parseParams(FullHttpRequest fullHttpRequest) {
        if (fullHttpRequest == null) {
            return Collections.emptyMap();
        }
        HttpMethod method = fullHttpRequest.method();

        Map<String, String> parmMap = new HashMap<>();

        if (HttpMethod.GET == method) {
            // 是GET请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            decoder.parameters().forEach((key, value) -> {
                // entry.getValue()是一个List, 只取第一个元素
                parmMap.put(key, value.get(0));
            });
        } else if (HttpMethod.POST == method) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullHttpRequest);
            decoder.offer(fullHttpRequest);
            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData parm : parmList) {
                Attribute data = (Attribute) parm;
                try {
                    parmMap.put(data.getName(), data.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 不支持其它方法
            throw new RuntimeException("不支持其它方法"); // 这是个自定义的异常, 可删掉这一行
        }

        return parmMap;
    }

}
