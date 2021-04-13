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

    @Override
    public Map<String, String> getHeaders(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.parseHeaders(fullHttpRequest);
    }

    @Override
    public Map<String, String> getParamMap(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.parseParams(fullHttpRequest);
    }

    @Override
    public String getBody(FullHttpRequest fullHttpRequest) {
        return FullHttpRequestUtils.fetchBody(fullHttpRequest);
    }
}
