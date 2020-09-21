package com.hanframework.mojito.config;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.http.HttpHeaders;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;
import com.hanframework.mojito.util.HttpRequestBuilder;
import org.junit.Test;

import java.net.URI;
import java.util.Map;

/**
 * @author liuxin
 * 2020-09-16 17:09
 */
public class HttpInstallerTest {


    /**
     * 构建http服务端
     */
    @Test
    public void testHttpServer() {
        Installer.httpServer((channel, request) -> {
            System.out.println("请求地址:" + request.getRequestURI());
            System.out.println("请求头" + request.getHeaders());
            System.out.println("请求参数:" + request.getRequestParams());
            return HttpResponseFacade.ok(request.getId());
        }).start(8080);
    }

    /**
     * 支持长连接
     *
     * @throws Exception 异常
     */
    @Test
    public void testHttpClient() throws Exception {
        Client<HttpRequestFacade, HttpResponseFacade> httpClient = Installer.httpClient();
        httpClient.connect("127.0.0.1", 8080);
        URI uri = new URI("/user/get");
        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder()
                .GET(uri)
                //设置长连接
                .addHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        for (int i = 0; i < 10; i++) {
            HttpRequestFacade httpRequestFacade = httpRequestBuilder.wrapBuild();
            System.out.println("request-id-request-" + i + "-" + httpRequestFacade.getId());
            MojitoFuture<HttpResponseFacade> httpResponseFacadeMojitoFuture = httpClient.sendAsync(httpRequestFacade);
            System.out.println("request-id-response-" + i + "-" + httpResponseFacadeMojitoFuture.get().getId());
        }
    }


}
