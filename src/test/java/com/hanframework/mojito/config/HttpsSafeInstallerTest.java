package com.hanframework.mojito.config;

import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;
import com.hanframework.mojito.server.handler.BusinessHandler;
import org.junit.Ignore;
import org.junit.Test;

import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Map;

/**
 * @author liuxin
 * 2020-09-16 17:09
 */
public class HttpsSafeInstallerTest {


    /**
     * https://csr.chinassl.net/decoder-csr.html
     * https://blog.csdn.net/tiandyoin/article/details/37880457
     * <p>
     * keytool -importcert -alias nginx17 -keystore cacerts -file nginx17.crt 将证书导入到jdk
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void testHttpsServer() throws Exception {
        File cert = new File("/Users/liuxin/Github/mojito/server.crt");
        File key = new File("/Users/liuxin/Github/mojito/server_pkcs8.key");
        Installer.httpsServer(subServerHandler(), cert, key).startAsync(8080);
    }

    private BusinessHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler() {
        return (channel, request) -> {
            System.out.println(request.getRequestURI());
            Map<String, String> requestParams = request.getRequestParams();
            System.out.println(requestParams);
            return HttpResponseFacade.TEXT("Hello");
        };
    }

    /**
     * 利用JDK的keytool 工具，生成服务端私钥和证书仓库。
     * 执行如下命令：
     * keytool -importkeystore -srckeystore server.jks -destkeystore server.jks -deststoretype pkcs12
     *
     * @throws Exception
     */
//    @Test
    public void HttpsServerTest() throws Exception {
        char[] passArray = "mima123".toCharArray();
        KeyStore ks = KeyStore.getInstance("TLSv1.2");
        //加载keytool 生成的文件
        FileInputStream inputStream = new FileInputStream("/Users/liuxin/Github/mojito/keystore.p12");
        ks.load(inputStream, passArray);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(ks, passArray);
        Installer.httpsServer(subServerHandler(), keyManagerFactory).startAsync(8080);
    }
}
