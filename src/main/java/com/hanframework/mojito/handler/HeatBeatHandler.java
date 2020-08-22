package com.hanframework.mojito.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author liuxin
 * 2020-08-01 15:16
 */
public class HeatBeatHandler extends ChannelInboundHandlerAdapter {

    @Override//用户事件触发
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {//如果接收到的事件消息属于我们之前定义的心跳事件
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;//将该事件消息强转为心跳事件

            //这里虽然监听了三种空闲,但是我们只对读写空闲做操作
            if (idleStateEvent.state() == IdleState.READER_IDLE) { //如果是读空闲
                System.out.println("读空闲事件触发...");
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                System.out.println("写空闲事件触发...");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                System.out.println("===============================================");
                System.out.println("读写空闲事件触发...");
                System.out.println("关闭该通道资源");
                ctx.channel().close();//关闭该通道
            }
        }
    }
}