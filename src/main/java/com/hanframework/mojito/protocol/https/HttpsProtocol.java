package com.hanframework.mojito.protocol.https;

import com.hanframework.mojito.protocol.http.HttpProtocol;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
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

    public HttpsProtocol(SslContext context) {
        this.context = context;
    }

    public SslContext getContext() {
        return context;
    }
}
