package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.protocol.mojito.model.NetType;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.protocol.mojito.model.RpcTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author liuxin
 * 2020-08-02 01:26
 */
public class HeartBeatRespHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest)
            throws Exception {
        //1. 如果是心跳请求,就给响应
        if (rpcRequest.getType() == NetType.HEARTBEAT.getType()) {
            RpcResponse response = RpcTools.buildHeartBeatResponse(rpcRequest);
            ctx.writeAndFlush(response);
        } else {
            //2. 否则下一个处理
            ctx.fireChannelRead(rpcRequest);
        }
    }


}
