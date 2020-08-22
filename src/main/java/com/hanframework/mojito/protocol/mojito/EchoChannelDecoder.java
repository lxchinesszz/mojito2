package com.hanframework.mojito.protocol.mojito;

import com.hanframework.mojito.protocol.ChannelDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author liuxin
 * 2020-08-22 23:13
 */
public class EchoChannelDecoder extends ChannelDecoder {

    public EchoChannelDecoder(String name) {
        super(name);
    }

    @Override
    protected void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.isReadable()) {
            int readableBytes = in.readableBytes();
            byte[] dataArr = new byte[readableBytes];
            in.readBytes(dataArr, 0, readableBytes);
            out.add(new String(dataArr));
        }
    }
}
