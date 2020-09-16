package com.hanframework.mojito.protocol.https;

import com.hanframework.mojito.protocol.http.HttpProtocol;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.File;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * SSL/TLS介绍
 * - SSL是安全套接层(secure sockets layer)，TLS是SSL的继任者，叫传输层安全(transport layer security)。
 * - SSL1.0: 已废除
 * - SSL2.0: RFC6176,已废除
 * - SSL3.0: RFC6101,基本废除
 * - TLS1.0: RFC2246,目前大都采用此种方式
 * - TLS1.1: RFC4346
 * - TLS1.2: RFC5246,没有广泛使用
 * - TLS1.3: IETF正在酝酿中
 * SSL常用认证方式介绍
 * 1. 单向认证
 * 单向认证只需客户端验证服务端，即客户端只需要认证服务端的合法性，服务端不需要。
 * 这种认证方式适用Web应用，因为web应用的用户数目广泛，且无需在通讯层对用户身份
 * 进行验证，一般都在应用逻辑层来保证用户的合法登入。但如果是企业应用对接，情况就
 * 不一样，可能会要求对客户端(相对而言)做身份验证。这时就需要做SSL双向认证。
 * 2. 双向认证
 * 双向认证顾名思义，服务端也需要认证客户端的合法性，这就意味着客户端的自签名证书
 * 需要导入服务端的数字证书仓库。采用这种方式会不太便利，一但客户端或者服务端修改
 * 了秘钥和证书，就需要重新进行证书交换，对于调试和维护工作量非常大。
 * 3. CA认证（缺点:花钱）
 * CA认证的好处是只要服务端和客户端只需要将CA证书导入各自的keystore，客户端和服
 * 务端只需判断这些证书是CA签名过的即可，这也是蜂鸟平台内部采用的认证方式
 *
 * @author liuxin
 * 2020-09-14 19:26
 */
public class HttpsProtocol extends HttpProtocol {

    private final SslContext context;

    public HttpsProtocol(File certificate, File privateKey) throws SSLException {
        this.context = SslContextBuilder.forServer(certificate, privateKey).build();
    }

    public HttpsProtocol(PrivateKey key, X509Certificate... keyCertChain) throws SSLException {
        this.context = SslContextBuilder.forServer(key, keyCertChain).build();
    }

    public HttpsProtocol(File keyCertChainFile, File keyFile, String keyPassword) throws SSLException {
        this.context = SslContextBuilder.forServer(keyCertChainFile, keyFile, keyPassword).build();
    }

    public HttpsProtocol(InputStream keyCertChainInputStream, InputStream keyInputStream, String keyPassword) throws SSLException {
        this.context = SslContextBuilder.forServer(keyCertChainInputStream, keyInputStream, keyPassword).build();
    }

    public HttpsProtocol(KeyManagerFactory keyManagerFactory) throws SSLException {
        this.context = SslContextBuilder.forServer(keyManagerFactory).build();
    }

    public HttpsProtocol(SslContext context) {
        this.context = context;
    }

    public SslContext getContext() {
        return context;
    }
}
