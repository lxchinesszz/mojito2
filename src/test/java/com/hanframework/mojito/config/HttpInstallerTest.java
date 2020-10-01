package com.hanframework.mojito.config;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.http.HttpHeaders;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;
import com.hanframework.mojito.util.HttpRequestBuilder;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.junit.Test;

import java.io.IOException;
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
    public void testHttpServer() throws Exception {

        Installer.httpServer((channelContext, request) -> {
            System.out.println("请求地址:" + request.getRequestURI());
            System.out.println("请求头" + request.getHeaders());
            System.out.println("请求参数:" + request.getRequestParams());
            System.out.println("请求body:" + request.getBody());
            System.out.println("请求方法:" + request.method());
            if (!request.getRequestParams().get("name").equalsIgnoreCase("mojito")) {
                throw new RuntimeException("检查是否可以读取参数");
            }
            return HttpResponseFacade.HTML("<h1>Hello</h1>");
        }).start(8080);

        testHttpClient();

        okHttpClientGetTest();

        okHttpClientPostTest();
    }


    private void okHttpClientGetTest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Builder().url("http://127.0.0.1:8080?name=mojito").get().build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // ... handle failed request
                System.out.println("发送失败");
            } else {
                String responseBody = response.body().string();
                System.out.println("OkHttpClient处理返回:" + responseBody);
            }
        } catch (IOException e) {
            // ... handle IO exception
        }
    }

    private void okHttpClientPostTest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("name", "mojito").build();
        Request request = new Builder().url("http://127.0.0.1:8080").post(formBody).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // ... handle failed request
                System.out.println("发送失败");
            } else {
                String responseBody = response.body().string();
                System.out.println("OkHttpClient处理返回:" + responseBody);
            }
        } catch (IOException e) {
            // ... handle IO exception
        }
    }

    /**
     * 支持长连接
     *
     * @throws Exception 异常
     */
    private void testHttpClient() throws Exception {
        Client<HttpRequestFacade, HttpResponseFacade> httpClient = Installer.httpClient();
        httpClient.connect("127.0.0.1", 8080);
        URI uri = new URI("/user/get?name=mojito");
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
        httpClient.close();
    }


}
