package com.hanframework.mojito.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


/**
 * Netty 调用 自定义的处理API,主要用于数据交换
 * 原生线程模型非常干净,我们通过重新定义Channel的方式,使其与容器处理逻辑所隔离。通过将Netty原生Channel 转换成 HanChannel
 * 所有的请求,都会在这里被处理。
 * 将Netty通道API转换为自己的API
 * 1. 连接的保存
 * 2. 读写的执行
 * 3. 异常的处理
 *
 * @author liuxin
 * @version Id: ServerHandler.java, v 0.1 2019-03-27 10:58
 */
@ChannelHandler.Sharable
public class NettySharableExchangeServerOutboundHandler extends ChannelOutboundWriteHandler {


    private ExchangeChannelHandler exchangeChannelHandler;

    public NettySharableExchangeServerOutboundHandler(ExchangeChannelHandler exchangeChannelHandler) {
        this.exchangeChannelHandler = exchangeChannelHandler;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            super.write(ctx, msg,promise);
            if (promise.cause() != null) {
                promise.cause().printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //连接断开就移除
        }
    }

}
