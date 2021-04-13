package com.hanframework.mojito.client.netty;

import com.hanframework.kit.util.StopWatch;
import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.protocol.mojito.MojitoProtocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.serialization.SerializeEnum;
import com.hanframework.mojito.server.handler.BusinessHandler;
import com.hanframework.mojito.server.impl.NettyServer;
import com.hanframework.mojito.test.pojo.User;
import com.hanframework.mojito.test.service.UserService;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.*;


/**
 * @author liuxin
 * 2020-08-01 17:38
 */
public class DefaultNettyClientTest {

    private Protocol<RpcRequest, RpcResponse> createProtocol() {
        return new MojitoProtocol((channel, request) -> new RpcResponse());
    }

    private void createServer(Protocol protocol) {
        NettyServer nettyServer = new NettyServer(protocol);
        nettyServer.startAsync(8888);
    }

    @Test
    @Ignore
    public void testNettyClient() throws Exception {
        Protocol<RpcRequest, RpcResponse> protocol = createProtocol();
        createServer(protocol);

        DefaultNettyClient<RpcRequest, RpcResponse> defaultNettyClient = new MojitoNettyClient(protocol);
        defaultNettyClient.connect("127.0.0.1", 8888);
        StopWatch stopWatch = new StopWatch();
        for (int i = 1; i <= 1; i++) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setProtocolType(ProtocolEnum.MOJITO.getType());
            rpcRequest.setSerializationType(SerializeEnum.HESSION2.getType());
            rpcRequest.setReturnType(User.class);
            rpcRequest.setServiceType(UserService.class);
            stopWatch.start("第" + i + "次");
            MojitoFuture<RpcResponse> future = defaultNettyClient.sendAsync(rpcRequest);
            RpcResponse response = future.get();
            System.out.println(response);
            System.out.println(future.get());
            stopWatch.stop();
        }
        System.out.println(stopWatch.prettyPrint());
        defaultNettyClient.close();
    }

    @Test
    @Ignore
    public void testNettyClientTimeout() throws Exception {
        Protocol<RpcRequest, RpcResponse> protocol = createProtocol();
        createServer(protocol);

        MojitoNettyClient abstractNettyClient = new MojitoNettyClient(protocol);
        abstractNettyClient.connect("127.0.0.1", 8888);
        StopWatch stopWatch = new StopWatch();
        for (int i = 1; i <= 1; i++) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setProtocolType(ProtocolEnum.MOJITO.getType());
            rpcRequest.setSerializationType(SerializeEnum.HESSION2.getType());
            rpcRequest.setReturnType(User.class);
            rpcRequest.setServiceType(UserService.class);
            stopWatch.start("第" + i + "次");
            MojitoFuture<RpcResponse> future = abstractNettyClient.sendAsync(rpcRequest);
            System.out.println(future.get(10, TimeUnit.MILLISECONDS));
            stopWatch.stop();
        }
        System.out.println(stopWatch.prettyPrint());
        abstractNettyClient.close();
    }


}