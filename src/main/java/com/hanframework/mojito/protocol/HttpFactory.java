package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.http.*;
import com.hanframework.mojito.server.handler.BusinessHandler;

/**
 * @author liuxin
 * 2020-09-02 14:17
 */
public class HttpFactory extends AbstractFactory<HttpRequestFacade, HttpResponseFacade> {

    public HttpFactory(Protocol<HttpRequestFacade, HttpResponseFacade> protocol) {
        super(protocol);
    }

    public HttpFactory(BusinessHandler<HttpRequestFacade, HttpResponseFacade> businessHandler) {
        super(new HttpProtocol(businessHandler), businessHandler);
    }

    public HttpFactory(Protocol<HttpRequestFacade, HttpResponseFacade> protocol, BusinessHandler<HttpRequestFacade, HttpResponseFacade> businessHandler) {
        super(protocol, businessHandler);
    }
}
