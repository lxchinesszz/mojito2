package com.hanframework.mojito.config;

import com.hanframework.kit.thread.HanThreadPoolExecutor;
import com.hanframework.kit.thread.NamedThreadFactory;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.AbstractAsyncClientPromiseHandler;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.client.netty.DefaultNettyClient;
import com.hanframework.mojito.handler.ExchangeChannelHandler;
import com.hanframework.mojito.handler.SingletonExchangeChannelHandler;
import com.hanframework.mojito.processor.RequestProcessor;
import com.hanframework.mojito.processor.ResponseProcessor;
import com.hanframework.mojito.protocol.*;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;
import com.hanframework.mojito.protocol.https.HttpsProtocol;
import com.hanframework.mojito.protocol.mojito.MojitoChannelDecoder;
import com.hanframework.mojito.protocol.mojito.MojitoChannelEncoder;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.DefaultServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;
import com.hanframework.mojito.server.handler.SubServerHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * 提供快速构建服务端和客户端的工具类
 * 1. 如果是服务端,必须要通过serverHandler编写服务端的handler实现,处理来自客户端的请求
 * 2. 如果是客户端,可以定制发送前后和接受前后的数据处理。
 *
 * @author liuxin
 * 2020-08-23 20:47
 */
public class Installer<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {

    private CodecFactory<T, V> codecFactory;

    private Class<T> requestType;

    private Class<V> responseType;

    private Installer(CodecFactory<T, V> codecFactory) {
        this.codecFactory = codecFactory;
    }

    /**
     * 构建一个支持http协议的服务器
     * 1. 生成ssl私钥  openssl genrsa -out server.key 2048
     * 2. 生政自签名证书 openssl req -new -sha256 -x509 -days 365 -key server.key -out server.crt
     *
     * @param subServerHandler 服务处理器
     * @param certificate      证书文件
     * @param privateKey       私钥
     * @return Server
     * @throws SSLException ssl异常
     */
    public static Server httpsServer(SubServerHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler, File certificate, File privateKey) throws SSLException {
        HttpsProtocol httpsProtocol = new HttpsProtocol(certificate, privateKey);
        Installer<HttpRequestFacade, HttpResponseFacade> tvInstaller = new Installer<>(new HttpCodecFactory(httpsProtocol, subServerHandler));
        tvInstaller.setRequestType(HttpRequestFacade.class);
        tvInstaller.setResponseType(HttpResponseFacade.class);
        return tvInstaller.getCodecFactory().getServer();
    }


    /**
     * 构建一个支持http协议的服务器
     *
     * @param subServerHandler  服务处理器
     * @param keyManagerFactory 秘钥管理器
     * @return Server
     * @throws SSLException ssl异常
     */
    public static Server httpsServer(SubServerHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler, KeyManagerFactory keyManagerFactory) throws SSLException {
        HttpsProtocol httpsProtocol = new HttpsProtocol(keyManagerFactory);
        Installer<HttpRequestFacade, HttpResponseFacade> tvInstaller = new Installer<>(new HttpCodecFactory(httpsProtocol, subServerHandler));
        tvInstaller.setRequestType(HttpRequestFacade.class);
        tvInstaller.setResponseType(HttpResponseFacade.class);
        return tvInstaller.getCodecFactory().getServer();
    }


    /**
     * 构建一个支持http协议的服务器
     *
     * @param subServerHandler 服务处理器
     * @return Server
     */
    public static Server httpServer(SubServerHandler<HttpRequestFacade, HttpResponseFacade> subServerHandler) {
        Installer<HttpRequestFacade, HttpResponseFacade> tvInstaller = new Installer<>(new HttpCodecFactory(subServerHandler));
        tvInstaller.setRequestType(HttpRequestFacade.class);
        tvInstaller.setResponseType(HttpResponseFacade.class);
        return tvInstaller.getCodecFactory().getServer();
    }

    /**
     * 构建一个http客户端
     *
     * @return Client
     */
    public static Client<HttpRequestFacade, HttpResponseFacade> httpClient() {
        Installer<HttpRequestFacade, HttpResponseFacade> tvInstaller = new Installer<>(new HttpCodecFactory());
        tvInstaller.setRequestType(HttpRequestFacade.class);
        tvInstaller.setResponseType(HttpResponseFacade.class);
        return tvInstaller.getCodecFactory().getClient();
    }


    /**
     * 使用该方法需要注意如果是服务端,一定要调用serverHandler方法创建
     *
     * @param requestType  定义的请求数据类型
     * @param responseType 定义的响应数据类型
     * @return ModuleConfig 模型
     */
    public static <T extends RpcProtocolHeader, V extends RpcProtocolHeader> ModuleConfig<T, V> modules(Class<T> requestType, Class<V> responseType) {
        return new ModuleConfig<>(Installer.module(requestType, responseType));
    }

    /**
     * @param requestType  定义的请求数据类型
     * @param responseType 定义的响应数据类型
     * @return ServerConfig 服务端配置器
     */
    public static <T extends RpcProtocolHeader, V extends RpcProtocolHeader> ServerConfig<T, V> server(Class<T> requestType, Class<V> responseType) {
        return new ServerConfig<>(Installer.module(requestType, responseType));
    }

    /**
     * @param requestType  定义的请求数据类型
     * @param responseType 定义的响应数据类型
     * @return ServerConfig 客户端配置器
     */
    public static <T extends RpcProtocolHeader, V extends RpcProtocolHeader> ClientConfig<T, V> client(Class<T> requestType, Class<V> responseType) {
        return new ClientConfig<>(Installer.module(requestType, responseType));
    }

    public static class ModuleConfig<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {

        private Installer<T, V> config;

        private ModuleConfig(Installer<T, V> config) {
            this.config = config;
        }

        public ModuleConfig<T, V> serverHandler(SubServerHandler<T, V> serverHandler) {
            this.config.serverHandler(serverHandler);
            return this;
        }

        public CodecFactory<T, V> create() {
            return config.getCodecFactory();
        }
    }


    public static class ServerConfig<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {

        private Installer<T, V> config;

        private ServerConfig(Installer<T, V> config) {
            this.config = config;
        }

        public ServerCreator<T, V> serverHandler(SubServerHandler<T, V> serverHandler) {
            this.config.serverHandler(serverHandler);
            return new ServerCreator<>(this.config);
        }

    }

    public static class ClientCreator<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {

        private Installer<T, V> config;

        private ClientCreator(Installer<T, V> serverConfig) {
            this.config = serverConfig;
        }


        public Client<T, V> create() {
            return config.getCodecFactory().getClient();
        }

        public Client<T, V> connect(String remoteHost, int remotePort) throws Exception {
            return config.getCodecFactory().getClient(remoteHost, remotePort);
        }

    }

    public static class ServerCreator<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {

        private Installer<T, V> config;

        private ServerCreator(Installer<T, V> serverConfig) {
            this.config = serverConfig;
        }

        public Server create() {
            return config.getCodecFactory().getServer();
        }
    }

    public static class ClientConfig<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {
        private Installer<T, V> config;

        private ClientConfig(Installer<T, V> config) {
            this.config = config;
        }

        public ClientConfig<T, V> addRequestProcessor(RequestProcessor<T>... requestProcessors) {
            this.config.getCodecFactory().setRequestProcessor(requestProcessors);
            return this;
        }

        public ClientConfig<T, V> addResponseProcessor(ResponseProcessor<V>... responseProcessors) {
            this.config.getCodecFactory().setResponseProcessor(responseProcessors);
            return this;
        }


        public Client<T, V> create() {
            return new ClientCreator<>(this.config).create();
        }

        public Client<T, V> conncet(String remoteHost, int remotePort) throws Exception {
            return new ClientCreator<>(this.config).connect(remoteHost, remotePort);
        }
    }

    private static <T extends RpcProtocolHeader, V extends RpcProtocolHeader> Installer<T, V> module(Class<T> requestType, Class<V> responseType) {

        Protocol<T, V> customerModelProtocol = new Protocol<T, V>() {

            private Executor executor = new HanThreadPoolExecutor(new NamedThreadFactory("mojimo")).getExecutory();

            private ServerHandler<T, V> serverHandler;

            private ClientPromiseHandler<T, V> clientPromiseHandler;

            @Override
            public String name() {
                return "CustomerModel";
            }

            @Override
            public ExchangeChannelHandler getExchangeChannelHandler() {
                return new SingletonExchangeChannelHandler(this);
            }

            @Override
            public Executor getExecutor() {
                return executor;
            }

            @Override
            public ChannelDecoder getRequestDecoder() {
                return new MojitoChannelDecoder("MojitoChannelDecoder");
            }

            @Override
            public ChannelEncoder getResponseEncoder() {
                return new MojitoChannelEncoder("MojitoChannelEncoder");
            }

            @Override
            public ServerHandler<T, V> getServerHandler() {
                if (Objects.isNull(this.serverHandler)) {
                    this.serverHandler = new DefaultServerHandler<>();
                }
                return this.serverHandler;
            }

            @Override
            public void setServerHandler(ServerHandler<T, V> serverHandler) {
                this.serverHandler = serverHandler;
            }

            @Override
            public ClientPromiseHandler<T, V> getClientPromiseHandler() {
                if (Objects.isNull(this.clientPromiseHandler)) {
                    this.clientPromiseHandler = new AbstractAsyncClientPromiseHandler<>();
                }
                return this.clientPromiseHandler;
            }
        };

        CodecFactory<T, V> codecFactory = new AbstractCodecFactory<T, V>(customerModelProtocol) {

            @Override
            public Client<T, V> getClient() {
                return new DefaultNettyClient<T, V>(customerModelProtocol) {
                    @Override
                    public void connect(String remoteHost, int remotePort) throws Exception {
                        super.connect(remoteHost, remotePort);
                    }
                };
            }


        };
        Installer<T, V> tvInstaller = new Installer<>(codecFactory);
        tvInstaller.setRequestType(requestType);
        tvInstaller.setResponseType(responseType);
        return tvInstaller;
    }

    private void serverHandler(SubServerHandler<T, V> serverHandler) {
        codecFactory.getServerHandler().initWrapper(serverHandler);
    }


    public Class<T> getRequestType() {
        return requestType;
    }

    private void setRequestType(Class<T> requestType) {
        this.requestType = requestType;
    }

    public Class<V> getResponseType() {
        return responseType;
    }

    private void setResponseType(Class<V> responseType) {
        this.responseType = responseType;
    }

    private CodecFactory<T, V> getCodecFactory() {
        return this.codecFactory;
    }


}
