package com.hanframework.mojito.server.impl;

import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.client.handler.ClientHandler;
import com.hanframework.mojito.client.handler.DefaultAsyncClientHandler;
import com.hanframework.mojito.client.netty.DefaultNettyClient;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.*;
import com.hanframework.mojito.protocol.echo.EchoProtocol;
import com.hanframework.mojito.protocol.mojito.MojitoProtocol;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.DefaultServerHandler;
import com.hanframework.mojito.server.handler.ServerHandler;
import org.junit.Test;

/**
 * 1. 编码器和解析器
 * 2. 定义服务端逻辑和客户端逻辑
 *
 * @author liuxin
 * 2020-07-31 19:15
 */
public class NettyServerTest {


    /**
     * 工厂方法用于自定义通信模型
     *
     * @return CodecFactory
     */
    private CodecFactory<RpcRequest, RpcResponse> codecFactory() {
        return new AbstractCodecFactory<RpcRequest, RpcResponse>() {
            @Override
            public ServerHandler<RpcRequest, RpcResponse> doServerHandler() {
                //1. 自定义服务端处理逻辑
                //   继承AbstractServerHandler,声明泛型
                return new DefaultServerHandler();
            }

            @Override
            public ClientHandler<RpcRequest, RpcResponse> doClientHandler() {
                //2. 自定义客户端处理逻辑
                //   继承AbstractAsyncClientHandler,声明泛型即可
                return new DefaultAsyncClientHandler();
            }

            @Override
            public Client<RpcRequest, RpcResponse> getClient() {
                //3. 自定义客户端处理逻辑
                //   继承AbstractNettyClient,声明泛型
                return new DefaultNettyClient(this);
            }
        };
    }

    class CustomerRpcInfo extends RpcProtocolHeader {
        private String name;

        public CustomerRpcInfo(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "CustomerRpcInfo{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private void customerFactory() throws Exception {
        CodecFactory<CustomerRpcInfo, CustomerRpcInfo> codecFactory = new AbstractCodecFactory<CustomerRpcInfo, CustomerRpcInfo>() {

            @Override
            public Client<CustomerRpcInfo, CustomerRpcInfo> getClient() {
                return null;
            }

            @Override
            public ServerHandler<CustomerRpcInfo, CustomerRpcInfo> doServerHandler() {
                return null;
            }

            @Override
            public ClientHandler<CustomerRpcInfo, CustomerRpcInfo> doClientHandler() {
                return null;
            }
        };
        Server server = codecFactory.getServer();
        Client<CustomerRpcInfo, CustomerRpcInfo> client = codecFactory.getClient();
        MojitoFuture<CustomerRpcInfo> customerRpcInfoMojitoFuture = client.sendAsync(new CustomerRpcInfo(""));
    }

    @Test
    public void clientTest() throws Exception {
        CodecFactory<RpcRequest, RpcResponse> codecFactory = codecFactory();
        Client<RpcRequest, RpcResponse> client = codecFactory.getClient();
        client.connect("127.0.0.1", 8888);
        client.sendAsync(new RpcRequest());
        MojitoFuture<RpcResponse> future = client.sendAsync(new RpcRequest());
        RpcResponse response = future.get();
        System.out.println(response);
    }


    /**
     * 服务端的组件基本都写好了。
     */
    @Test
    public void createServerTest2() {
        CodecFactory<RpcRequest, RpcResponse> codecFactory = codecFactory();
        Server server = codecFactory.getServer();
        server.start(8888);

    }


    /**
     * 服务端的组件基本都写好了。
     */
    @Test
    public void createServerTest1() {
        Server server = new NettyServer(new MojitoProtocol());
        //2. 服务支持的协议信息
        server.registerProtocol(new MojitoProtocol());
        server.start(8888);
    }

    @Test
    public void createEchoServerTest() {
        Server server = new NettyServer(new MojitoProtocol());
        //1. 将要提供对外服务类,生成签名信息,并交给server处理
        //2. 服务支持的协议信息
        server.registerProtocol(new EchoProtocol());
        server.start(8887);
    }
}