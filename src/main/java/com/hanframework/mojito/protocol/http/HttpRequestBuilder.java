package com.hanframework.mojito.protocol.http;

import com.google.common.base.Strings;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxin
 * 2020-09-17 00:23
 */
public class HttpRequestBuilder {

    private HttpVersion httpVersion = HttpVersion.HTTP_1_1;

    private HttpMethod httpMethod;

    private Map<String, Object> parameter = new HashMap<>();

    private String uri;

    private Map<String, String> headers = new HashMap<>();

    public HttpRequestBuilder GET(String uri, Map<String, Object> parameter) {
        this.httpMethod = HttpMethod.GET;
        this.parameter = parameter;
        this.uri = uri;
        return this;
    }

    public HttpRequestBuilder GET(URI uri) {
        return GET(uri.toASCIIString(), new HashMap<>());
    }

    public HttpRequestBuilder GET(URI uri, Map<String, Object> parameter) {
        return GET(uri.toASCIIString(), parameter);
    }

    public HttpRequestBuilder addHeader(String name, String value) {
        if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(value)) {
            return this;
        }
        headers.put(name, value);
        return this;
    }

    public FullHttpRequest build() {
        DefaultFullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(httpVersion, httpMethod, uri);
        headers.forEach((key, value) -> fullHttpRequest.headers().set(key, value));
        fullHttpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, fullHttpRequest.content().readableBytes());
        return fullHttpRequest;
    }

    public HttpRequestFacade wrapBuild() {
        return new HttpRequestFacade(build());
    }
}
