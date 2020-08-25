package com.hanframework.mojito.config;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.CodecFactory;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.AbstractServerHandler;
import org.junit.Test;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


/**
 * @author liuxin
 * 2020-08-23 21:34
 */
public class MojitoConfigTest implements Serializable {

    class RpcUserRequest extends RpcProtocolHeader {
        private String message;

        public RpcUserRequest(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "RpcUserRequest{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    class RpcUserResponse extends RpcProtocolHeader {
        private String message;

        public RpcUserResponse(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "RpcUserResponse{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }


    @Test
    public void clientTest2() throws Exception {
        //使用不匹配的模型,服务端会报错,直接断开连接。
        //TODO 是否服务端可以报错误信息返回给客户端?
        CodecFactory<RpcRequest, RpcResponse> codecFactory = MojitoConfig.client(RpcRequest.class, RpcResponse.class).create();
        Client<RpcRequest, RpcResponse> client = codecFactory.getClient("127.0.0.1", 12306);
        MojitoFuture<RpcResponse> async = client.sendAsync(new RpcRequest());
        System.out.println(async.get(5, TimeUnit.SECONDS));
        client.close();
    }

    @Test
    public void clientTest() throws Exception {
        CodecFactory<RpcUserRequest, RpcUserResponse> codecFactory = MojitoConfig.client(RpcUserRequest.class, RpcUserResponse.class).create();
        Client<RpcUserRequest, RpcUserResponse> client = codecFactory.getClient("127.0.0.1", 12306);
        MojitoFuture<RpcUserResponse> async = client.sendAsync(new RpcUserRequest("I'am RpcUser"));
        System.out.println(async.get());
        client.close();
    }

    @Test
    public void serverTest() throws Exception {
        CodecFactory<RpcUserRequest, RpcUserResponse> codecFactory = MojitoConfig.server(RpcUserRequest.class, RpcUserResponse.class)
                .serverHandler(new AbstractServerHandler<RpcUserRequest, RpcUserResponse>() {
                    @Override
                    public RpcUserResponse doHandler(EnhanceChannel channel,RpcUserRequest rpcRequest) throws Exception {
                        return new RpcUserResponse("服务端返回: " + rpcRequest.message);
                    }
                })
                .create();
        Server server = codecFactory.getServer();
        server.start(12306);
    }

    @Test
    public void mqTest(){
        //1. 服务端放一个路由键。当有人往指定的routeKey发消息，像
    }

}