package com.hanframework.mojito.protocol.mojito;

import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.serialization.Serialize;
import com.hanframework.mojito.serialization.SerializeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.net.SocketAddress;
import java.util.List;

/**
 * @author liuxin
 * 2020-07-31 19:34
 */
public class MojitoChannelDecoder extends ChannelDecoder {

    public MojitoChannelDecoder(String name) {
        super(name);
    }

    /**
     * 是否完整的消息头
     *
     * @param dataHeadSize 能读取到的数据
     * @return boolean
     */
    private boolean isFullMessageHeader(int dataHeadSize) {
        //因为一定会有数据所以只有大于5可能会完整,但是等于5一定不完整
        return dataHeadSize > 5;
    }

    /**
     * 是否完整消息
     *
     * @param buffer   数据buffer
     * @param dataSize
     * @return
     */
    private boolean isFullMessage(ByteBuf buffer, Integer dataSize) {
        //因为可能存在粘包的问题,所以大于等于就算本包完整了。
        return buffer.readableBytes() >= dataSize;
    }

    @Override
    protected void doDecode(ChannelHandlerContext ctx, ByteBuf inByteBuf, List<Object> out) throws Exception {
        byte[] dataArr;
        //1. 不可读就关闭
        if (!inByteBuf.isReadable()) {
            Channel channel = ctx.channel();
            SocketAddress socketAddress = channel.remoteAddress();
            channel.close();
            System.err.println(">>>>>>>>>[" + socketAddress + "]客户端已主动断开连接....");
            return;
        }
        //2. 可读的数据大小
        int dataHeadSize = inByteBuf.readableBytes();
        //3. 不是完整的数据头就直接返回
        if (!isFullMessageHeader(dataHeadSize)) {
            return;
        }
        //4. 完整的数据头就开始看数据长度是否满足
        inByteBuf.markReaderIndex();
        //协议类型
        byte protocolType = inByteBuf.readByte();
        //序列化类型
        byte serializationType = inByteBuf.readByte();
        //数据长度
        int dataSize = inByteBuf.readInt();
        //5. 拆包的直接返回下次数据完整了,在处理
        if (!isFullMessage(inByteBuf, dataSize)) {
            inByteBuf.resetReaderIndex();
            System.out.println();
            System.err.println("######################数据不足已重置buffer######################");
            return;
        }
        //6. 黏包的直接读取数据
        dataArr = new byte[dataSize];
        inByteBuf.readBytes(dataArr, 0, dataSize);
        //找到序列化器,性能有提升空间,可以序列化器可以进行池化
        SerializeEnum serializeEnum = SerializeEnum.ofByType(serializationType);
        Class<? extends Serialize> serialize = serializeEnum.getSerialize();
        //根据类型获取序列化器
        Serialize serializeNewInstance = serialize.newInstance();
        Object deserialize = serializeNewInstance.deserialize(dataArr);
        out.add(deserialize);
    }
}
