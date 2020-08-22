package com.hanframework.mojito.protocol.mojito;

import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.serialization.Serialize;
import com.hanframework.mojito.serialization.SerializeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 对服务端响应客户端的编码
 *
 * @author liuxin
 * 2020-07-31 19:34
 */
public class MojitoChannelEncoder extends ChannelEncoder<RpcProtocolHeader> {

    public MojitoChannelEncoder(String name) {
        super(name);
    }

    @Override
    protected void doEncode(ChannelHandlerContext ctx, RpcProtocolHeader msg, ByteBuf out) throws Exception {
        byte protocolType = msg.getProtocolType();
        byte serializationType = msg.getSerializationType();
        out.writeByte(protocolType);
        out.writeByte(serializationType);
        Serialize serialize = SerializeEnum.ofByType(serializationType).getSerialize().newInstance();
        byte[] data = serialize.serialize(msg);
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
