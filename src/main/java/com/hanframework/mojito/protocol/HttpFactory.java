package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.http.*;
import com.hanframework.mojito.server.handler.SubServerHandler;

/**
 * @author liuxin
 * 2020-09-02 14:17
 */
public class HttpFactory extends AbstractFactory<HttpRequestFacade, HttpResponseFacade> {

    public HttpFactory(){
        this(new HttpProtocol());
    }
    public HttpFactory(Protocol<HttpRequestFacade, HttpResponseFacade> protocol) {
        super(protocol);
    }

    public HttpFactory(SubServerHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler) {
        super(new HttpProtocol(), subServerHandler);
    }

    public HttpFactory(Protocol<HttpRequestFacade, HttpResponseFacade> protocol, SubServerHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler) {
        super(protocol,subServerHandler);
    }
}
