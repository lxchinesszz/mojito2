package com.hanframework.mojito.protocol.http;

import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxin
 * 2020-09-02 14:18
 */
public final class HttpRequestFacade extends RpcProtocolHeader {

    private final FullHttpRequest fullHttpRequest;

    private Map<String, String> paramMap;


    public HttpRequestFacade(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }

    public Map<String, String> getRequestParams() {
        if (paramMap == null) {
            paramMap = parse();
        }
        return paramMap;
    }

    /**
     * 是否长连接
     *
     * @return
     */
    public boolean KeepAlive() {
        String keepAlive = fullHttpRequest.headers().get("Connection", "false");
        return Boolean.parseBoolean(keepAlive);
    }

    private Map<String, String> parse() {
        HttpMethod method = fullHttpRequest.method();

        Map<String, String> parmMap = new HashMap<>();

        if (HttpMethod.GET == method) {
            // 是GET请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            decoder.parameters().entrySet().forEach(entry -> {
                // entry.getValue()是一个List, 只取第一个元素
                parmMap.put(entry.getKey(), entry.getValue().get(0));
            });
        } else if (HttpMethod.POST == method) {
            // 是POST请求
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
