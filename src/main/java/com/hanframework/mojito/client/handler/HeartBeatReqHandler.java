package com.hanframework.mojito.client.handler;

import com.hanframework.mojito.protocol.mojito.model.RpcTools;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author liuxin
 * 2020-08-02 01:18
 */
public class HeartBeatReqHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("Read   空闲未收到服务端数据,就发送心跳");
//                ctx.disconnect();
                ctx.writeAndFlush(RpcTools.buildHeartBeatRequest());
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("Write  空闲");
                ctx.writeAndFlush(RpcTools.buildHeartBeatRequest());
            }
        }
    }
}
