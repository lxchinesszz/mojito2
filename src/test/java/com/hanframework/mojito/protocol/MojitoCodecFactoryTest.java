package com.hanframework.mojito.protocol;


import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.server.Server;
import com.hanframework.mojito.server.handler.BusinessHandler;
import com.hanframework.mojito.server.handler.ChannelContext;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author liuxin
 * 2020-09-02 17:38
 */
public class MojitoCodecFactoryTest {

    @Test
    @Ignore
    public void testMojitoServer() throws Exception {
        MojitoFactory mojitoCodecFactory = new MojitoFactory((channel, rpcRequest) -> {
            RpcResponse response = new RpcResponse();
            response.setMessage("hello");
            return response;
        });
        Server server = mojitoCodecFactory.getServer();
        server.startAsync(12310);

        testMojitoClient();
    }


    public void testMojitoClient() throws Exception {
        MojitoFactory mojitoCodecFactory = new MojitoFactory(new BusinessHandler<RpcRequest, RpcResponse>() {
            @Override
            public RpcResponse handler(ChannelContext channelContext, RpcRequest rpcRequest) throws RemotingException {
                RpcResponse response = new RpcResponse();
                response.setMessage("hello");
                return response;
            }
        });

        Client<RpcRequest, RpcResponse> client = mojitoCodecFactory.getClient();
        client.connect("127.0.0.1", 12310);
        RpcRequest rpcRequest = new RpcRequest();
        MojitoFuture<RpcResponse> future = client.sendAsync(rpcRequest);
        RpcResponse response = future.get();
        System.out.println(response);
    }

}