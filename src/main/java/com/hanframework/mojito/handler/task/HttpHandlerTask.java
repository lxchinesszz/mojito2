package com.hanframework.mojito.handler.task;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;
import com.hanframework.mojito.server.handler.ServerHandler;

/**
 * @author liuxin
 * 2020-09-14 17:46
 */
public class HttpHandlerTask extends AbstractHandlerTask<HttpRequestFacade, HttpResponseFacade> {

    public HttpHandlerTask(ServerHandler<HttpRequestFacade, HttpResponseFacade> serverHandler, EnhanceChannel enhanceChannel, HttpRequestFacade request) {
        super(serverHandler, enhanceChannel, request);
    }

    @Override
    public HttpResponseFacade doResult() {
        return getServerHandler().handler(getEnhanceChannel(), getRequest());
    }

}
