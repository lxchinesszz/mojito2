package com.hanframework.mojito.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 请求解码器负责将二进制数据转换成能处理的协议
 *
 * @author liuxin
 * 2020-07-25 22:03
 */
public abstract class ChannelDecoder extends ByteToMessageDecoder {

    private String name;

    public ChannelDecoder(String name) {
        this.name = name;
    }

    /**
     * 解码方法
     *
     * @param ctx 通道上下文信息
     * @param in  网络传过来的信息(注意粘包和拆包问题)
     * @param out in中的数据转换成对象调用out.add方法
     * @throws Exception
     */
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("二进制数据已进入到" + name + "解码器");
        doDecode(ctx, in, out);
    }


    /**
     * 解码方法
     *
     * @param ctx 通道上下文信息
     * @param in  网络传过来的信息(注意粘包和拆包问题)
     * @param out in中的数据转换成对象调用out.add方法
     * @throws Exception
     */
    protected abstract void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception;
}
