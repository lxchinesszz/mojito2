package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author liuxin
 * 2020-07-25 22:03
 */
public abstract class ChannelEncoder<T extends RpcProtocolHeader> extends MessageToByteEncoder<T> {

    private String name;

    public ChannelEncoder(String name) {
        this.name = name;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception {
        System.out.println("发送数据已进入到" + name + "编码器中:" + msg);
        doEncode(ctx, msg, out);
    }

    protected abstract void doEncode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception;
}
