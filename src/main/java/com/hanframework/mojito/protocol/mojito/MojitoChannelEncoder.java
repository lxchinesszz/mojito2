package com.hanframework.mojito.protocol.mojito;

import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.serialization.Serializer;
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
        //1. 获取协议类型(1个字节)
        out.writeByte(msg.getProtocolType());
        //2. 获取序列化类型(1个字节)
        out.writeByte(msg.getSerializationType());
        //3. 根据序列化类型找到数据转换器生成二进制数据
        Serializer serializer = SerializeEnum.ofByType(msg.getSerializationType()).getSerialize().newInstance();
        byte[] data = serializer.serialize(msg);
        //4. 写入报文长度(4个字节)
        out.writeInt(data.length);
        //5. 写入报文内容(数组)
        out.writeBytes(data);
    }
}
