package com.hanframework.mojito.config;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.AbstractAsyncClientPromiseHandler;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.client.netty.AbstractNettyClient;
import com.hanframework.mojito.protocol.AbstractCodecFactory;
import com.hanframework.mojito.protocol.CodecFactory;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.handler.AbstractServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;

/**
 * @author liuxin
 * 2020-08-23 20:47
 */
public class MojitoConfig<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {

    private CodecFactory<T, V> codecFactory;

    private Class<T> requestType;


    private Class<V> responseType;


    private MojitoConfig(CodecFactory<T, V> codecFactory) {
        this.codecFactory = codecFactory;
    }

    public static <T extends RpcProtocolHeader, V extends RpcProtocolHeader> ServerConfig<T, V> server(Class<T> requestType, Class<V> responseType) {
        MojitoConfig<T, V> mojitoConfig = MojitoConfig.module(requestType, responseType);
        return new ServerConfig<>(mojitoConfig);
    }

    public static <T extends RpcProtocolHeader, V extends RpcProtocolHeader> ClientConfig<T, V> client(Class<T> requestType, Class<V> responseType) {
        MojitoConfig<T, V> mojitoConfig = MojitoConfig.module(requestType, responseType);
        return new ClientConfig<>(mojitoConfig);
    }


    public static class ServerConfig<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {

        private MojitoConfig<T, V> mojitoConfig;

        private ServerConfig(MojitoConfig<T, V> mojitoConfig) {
            this.mojitoConfig = mojitoConfig;
        }

        public ServerConfig<T, V> serverHandler(AbstractServerHandler<T, V> serverHandler) {
            this.mojitoConfig.serverHandler(serverHandler);
            return this;
        }

        public CodecFactory<T, V> create() {
            return mojitoConfig.create();
        }

    }

    public static class ClientConfig<T extends RpcProtocolHeader, V extends RpcProtocolHeader> {
        private MojitoConfig<T, V> mojitoConfig;

        private ClientConfig(MojitoConfig<T, V> mojitoConfig) {
            this.mojitoConfig = mojitoConfig;
        }

        public CodecFactory<T, V> create() {
            return mojitoConfig.create();
        }
    }

    private static <T extends RpcProtocolHeader, V extends RpcProtocolHeader> MojitoConfig<T, V> module(Class<T> requestType, Class<V> responseType) {
        CodecFactory<T, V> codecFactory = new AbstractCodecFactory<T, V>() {
            @Override
            public Client<T, V> getClient() {
                return new AbstractNettyClient<T, V>(this) {
                    @Override
                    public void connect(String remoteHost, int remotePort) throws Exception {
                        super.connect(remoteHost, remotePort);
                    }
                };
            }

            @Override
            public ServerHandler<T, V> doServerHandler() {
                //默认空实现
                return null;
            }

            @Override
            public ClientPromiseHandler<T, V> doClientHandler() {
                return new AbstractAsyncClientPromiseHandler<T, V>() {
                    @Override
                    public void sendBefore(T rpcRequest) {
                        //默认空实现
                    }

                    @Override
                    public void receivedBefore(V rpcResponse) {
                        //默认空实现
                    }
                };
            }
        };
        MojitoConfig<T, V> tvMojitoConfig = new MojitoConfig<>(codecFactory);
        tvMojitoConfig.setRequestType(requestType);
        tvMojitoConfig.setResponseType(responseType);
        return tvMojitoConfig;
    }

    private void serverHandler(AbstractServerHandler<T, V> serverHandler) {
        if (codecFactory.getServerHandler() != null) {
            throw new RuntimeException("不允许重复添加" + AbstractServerHandler.class);
        }
        codecFactory.setServerHandler(serverHandler);
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

    private CodecFactory<T, V> create() {
        return this.codecFactory;
    }


}
