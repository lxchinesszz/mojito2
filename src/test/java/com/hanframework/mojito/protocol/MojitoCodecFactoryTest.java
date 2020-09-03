package com.hanframework.mojito.protocol;


import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.SubServerHandler;
import org.junit.Test;

/**
 * @author liuxin
 * 2020-09-02 17:38
 */
public class MojitoCodecFactoryTest {

    @Test
    public void testMojitoServer() {
        MojitoCodecFactory mojitoCodecFactory = new MojitoCodecFactory((channel, rpcRequest) -> {
            RpcResponse response = new RpcResponse();
            response.setMessage("hello");
            return response;
        });
        Server server = mojitoCodecFactory.getServer();
        server.start(12306);
    }


    @Test
    public void testMojitoClient() throws Exception {
        MojitoCodecFactory mojitoCodecFactory = new MojitoCodecFactory(new SubServerHandler<RpcRequest, RpcResponse>() {
            @Override
            public RpcResponse handler(EnhanceChannel channel, RpcRequest rpcRequest) throws RemotingException {
                RpcResponse response = new RpcResponse();
                response.setMessage("hello");
                return response;
            }
        });

        Client<RpcRequest, RpcResponse> client = mojitoCodecFactory.getClient();
        client.connect("127.0.0.1", 12306);
        RpcRequest rpcRequest = new RpcRequest();
        MojitoFuture<RpcResponse> future = client.sendAsync(rpcRequest);
        RpcResponse response = future.get();
        System.out.println(response);
    }

}