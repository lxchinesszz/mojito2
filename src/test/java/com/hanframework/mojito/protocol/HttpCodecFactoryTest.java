package com.hanframework.mojito.protocol;

import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.BusinessHandler;
import com.hanframework.mojito.server.handler.ChannelContext;
import org.junit.Test;

import java.util.Map;


/**
 * 验证http请求
 *
 * @author liuxin
 * 2020-09-02 14:26
 */
public class HttpCodecFactoryTest {

    @Test
    public void testHttpServer() {
        HttpFactory httpCodecFactory = new HttpFactory(new BusinessHandler<HttpRequestFacade, HttpResponseFacade>() {
            @Override
            public HttpResponseFacade handler(ChannelContext channel, HttpRequestFacade request) throws RemotingException {
                Map<String, String> requestParams = request.getRequestParams();
                System.out.println("请求参数:" + requestParams);
                return HttpResponseFacade.HTML("<h1>hello</h1>");
            }
        });
        Server server = httpCodecFactory.getServer();
        server.startAsync(8079);
    }
}