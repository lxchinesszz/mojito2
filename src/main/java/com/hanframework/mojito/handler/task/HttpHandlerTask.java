package com.hanframework.mojito.handler.task;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;
import com.hanframework.mojito.server.handler.ServerHandler;
import com.hanframework.mojito.util.RequestPeerMapping;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuxin
 * 2020-09-14 17:46
 */
@Slf4j
public class HttpHandlerTask extends AbstractHandlerTask<HttpRequestFacade, HttpResponseFacade> {

    public HttpHandlerTask(ServerHandler<HttpRequestFacade, HttpResponseFacade> serverHandler, EnhanceChannel enhanceChannel, HttpRequestFacade request) {
        super(serverHandler, enhanceChannel, request);
    }

    @Override
    public HttpResponseFacade doResult() {
        HttpRequestFacade httpRequestFacade = getRequest();
        httpRequestFacade.setId(httpRequestFacade.getOriginId());
        HttpResponseFacade httpResponseFacade = getServerHandler().handler(getEnhanceChannel(), httpRequestFacade);
        if (getRequest().keepAlive()) {
            FullHttpResponse fullHttpResponse = httpResponseFacade.getFullHttpResponse();
            fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        //拷贝请求id
        RequestPeerMapping.copyRequestId(httpRequestFacade, httpResponseFacade);
        log.debug("服务端收到的响应头request-id:" + httpRequestFacade.getOriginId());
        log.debug("服务端返回的响应头request-id:" + httpResponseFacade.getOriginId());
        return httpResponseFacade;
    }


}
