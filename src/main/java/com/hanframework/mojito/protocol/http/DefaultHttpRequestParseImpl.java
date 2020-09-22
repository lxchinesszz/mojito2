package com.hanframework.mojito.protocol.http;


import com.hanframework.mojito.util.FullHttpRequestUtils;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

/**
 * 重写该方法来解析http
 *
 * @author liuxin
 * 2020-09-22 21:47
 */
public class DefaultHttpRequestParseImpl extends AbstractHttpRequestParser {


    public Map<String, String> getHeaders(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.parseHeaders(fullHttpRequest);
    }

    public Map<String, String> getParamMap(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.parseParams(fullHttpRequest);
    }

    public HttpMethod getMethod(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.parseHttpMethod(fullHttpRequest);
    }

    public String getBody(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.fetchBody(fullHttpRequest);
    }
}
