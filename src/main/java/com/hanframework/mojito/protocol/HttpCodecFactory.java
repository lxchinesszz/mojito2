package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.http.*;
import com.hanframework.mojito.server.handler.SubServerHandler;

/**
 * @author liuxin
 * 2020-09-02 14:17
 */
public class HttpCodecFactory extends AbstractCodecFactory<HttpRequestFacade, HttpResponseFacade> {

    public HttpCodecFactory(){
        this(new HttpProtocol());
    }
    public HttpCodecFactory(Protocol<HttpRequestFacade, HttpResponseFacade> protocol) {
        super(protocol);
    }

    public HttpCodecFactory(SubServerHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler) {
        super(new HttpProtocol(), subServerHandler);
    }

    public HttpCodecFactory(Protocol<HttpRequestFacade, HttpResponseFacade> protocol, SubServerHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler) {
        super(protocol,subServerHandler);
    }
}
