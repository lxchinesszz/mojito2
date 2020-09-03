package com.hanframework.mojito.protocol.mojito;

import com.hanframework.kit.method.MethodParameter;
import com.hanframework.kit.reflection.MethodTools;
import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.protocol.ServiceURL;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.serialization.ProtostuffObjectSerialize;
import com.hanframework.mojito.serialization.Serialize;
import com.hanframework.mojito.serialization.SerializeEnum;
import com.hanframework.mojito.test.pojo.User;
import com.hanframework.mojito.test.service.UserService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author liuxin
 * 2020-07-31 21:52
 */
public class MojitoChannelDecoderTest {

    private ServiceURL createServiceURL() {
        return new ServiceURL();
    }

    /**
     * 主要依据:
     * 数据有
     * 协议类型(1位) + 序列化类型(1位) + 报文大小(4位) + 数据报文组成(N位)
     * *******************************************************************************
     * ----------------     -----------------   ----------------   ------------------
     * | 协议类型(1位) |   + | 序列化类型(1位) | + | 报文大小(4位) | + | 数据报文组成(N位)|
     * ----------------     -----------------   ----------------   ------------------
     * *******************************************************************************¬
     * 数据读取测试验证
     * 1. 完整数据报文
     * 2. 拆包的数据报文
     * 3. 粘包的数据报文
     *
     * @throws Exception 异常
     */
    @Test
    public void testDecoder() throws Exception {
        ServiceURL serviceURL = createServiceURL();
        //1. 测试完成的数据报文
        ByteBuf fullByteBuf = writeFullServiceURL(serviceURL);
        testServiceURLDecoder(fullByteBuf);
        //2. 测试完成的拆包的数据报文
        ByteBuf unpackingByteBuf = writeUnpackingServiceURL(serviceURL);
        testUnpackingServiceURLDecoder(unpackingByteBuf, serviceURL);
        //3. 测试粘包场景
        ByteBuf stickPackageByteBuf = writeStickPackageServiceURL(serviceURL);
        testServiceURLDecoder(stickPackageByteBuf);

    }


    @Test
    public void test2() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte((byte) i);
        }
        ByteBuf input = buffer.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assert channel.writeInbound(input.retain());
        assert channel.finish();
        ByteBuf read = (ByteBuf) channel.readInbound();
        assert buffer.readSlice(3).equals(read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assert buffer.readSlice(3).equals(read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assert buffer.readSlice(3).equals(read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assert read == null;
        buffer.release();
    }

    @Test
    public void testBufferAPI() {
        ServiceURL serviceURL = createServiceURL();
        serviceURL.setProtocolType(ProtocolEnum.MOJITO.getType());
        serviceURL.setSerializationType(SerializeEnum.PROTOSTUFF.getType());
        ByteBuf buffer = Unpooled.buffer();
        //协议头
        buffer.writeByte(serviceURL.getProtocolType());
        //序列化类型
        buffer.writeByte(serviceURL.getSerializationType());
        ProtostuffObjectSerialize protostuffObjectSerialize = new ProtostuffObjectSerialize();
        byte[] serialize = protostuffObjectSerialize.serialize(new User());
        //数据长度
        buffer.writeInt(serialize.length);
        buffer.writeBytes(serialize);

        System.out.println("可读的长度:" + buffer.readableBytes());
        System.out.println(buffer);

        System.out.println("读取协议头:" + buffer.readByte());
        System.out.println("序列化类型:" + buffer.readByte());
        System.out.println("数据长度:" + buffer.readInt());
        System.out.println(buffer);
        //重置读下标
        buffer.resetReaderIndex();

        System.out.println("读取协议头:" + buffer.readByte());
        System.out.println("序列化类型:" + buffer.readByte());
        int dataSize = buffer.readInt();
        System.out.println("数据长度:" + dataSize);
        System.out.println(buffer);
        byte[] data = new byte[dataSize];
        buffer.readBytes(data, 0, dataSize);

        User deserialize = protostuffObjectSerialize.deserialize(data, User.class);
        System.out.println(deserialize);

        System.out.println(buffer.readableBytes());
    }

    /**
     * 拆包问题验证
     */
    @Test
    public void unpackingTest() {
        //协议头+序列化头+长度+数据集合
        ByteBuf buffer = Unpooled.buffer();
        //协议头占用1个字节
        buffer.writeByte(1);//writeByte占用1个字节,大于byte会精度丢失。
        //序列化类型
        buffer.writeByte(2);

        ProtostuffObjectSerialize protostuffObjectSerialize = new ProtostuffObjectSerialize();
        byte[] serialize = protostuffObjectSerialize.serialize(new User());
        //数据长度
        buffer.writeInt(serialize.length);
        buffer.writeBytes(serialize);
        //writeInt占用4个字节,大于byte会精度丢失。
//        buffer.writeLong(23);//writeLong占用8个字节,大于byte会精度丢失。
        //数据头大小 = byte + byte + int = 6个字节
        int dataHeadSize = buffer.readableBytes();
        System.out.println("可读的长度:" + buffer.readableBytes());
        System.out.println("是否完整协议头:" + isFullMessageHeader(dataHeadSize));
        byte protocolType = buffer.readByte();
        byte serializationType = buffer.readByte();
        int dataSize = buffer.readInt();
        System.out.println("是否完整消息体:" + isFullMessage(buffer, dataSize));
//        //还原读的位置
//        buffer.resetReaderIndex();
        System.out.println("是否完整消息体:" + isFullMessage(buffer, dataSize));
        //还原读的位置
//        buffer.resetReaderIndex();
        if (isFullMessage(buffer, dataSize)) {
            //如果是完整的消息题就开始读数据
            byte[] data = new byte[dataSize];
            buffer.readBytes(data, 0, dataSize);
            User deserialize = protostuffObjectSerialize.deserialize(data, User.class);
            System.out.println("协议解析:" + deserialize);
        }
    }


    private boolean isFullMessage(ByteBuf buffer, Integer dataSize) {
        System.out.println(buffer.readableBytes());
        //因为可能存在粘包的问题,所以大于等于就算本包完整了。
        return buffer.readableBytes() >= dataSize;
    }

    private boolean isFullMessageHeader(int dataHeadSize) {
        //因为一定会有数据所以只有大于5可能会完整,但是等于5一定不完整
        return dataHeadSize > 5;
    }


    /**
     * 创建完整的数据报文
     *
     * @param serviceURL 服务
     * @return ByteBuf
     * @throws Exception
     */
    private ByteBuf writeFullServiceURL(ServiceURL serviceURL) throws Exception {
        //堆上创建或者堆外创建
        ByteBuf directBuffer = Unpooled.directBuffer();
        serviceURL.setProtocolType(ProtocolEnum.MOJITO.getType());
        serviceURL.setSerializationType(SerializeEnum.HESSION2.getType());
        Serialize serialize = SerializeEnum.ofByType(serviceURL.getSerializationType()).getSerialize().newInstance();
        byte[] serializeData = serialize.serialize(serviceURL);
        //协议头
        directBuffer.writeByte(serviceURL.getProtocolType());
        //序列化类型
        directBuffer.writeByte(serviceURL.getSerializationType());
        //数据长度
        directBuffer.writeInt(serializeData.length);
        //数据信息
        directBuffer.writeBytes(serializeData);
        return directBuffer;
    }


    /**
     * 粘包场景，两个请求在一个buf中
     *
     * @param serviceURL 服务
     * @return ByteBuf
     * @throws Exception 异常
     */
    private ByteBuf writeStickPackageServiceURL(ServiceURL serviceURL) throws Exception {
        ByteBuf byteBuf1 = writeFullServiceURL(serviceURL);
        serviceURL.setSerializationType(SerializeEnum.COMPACT.getType());
        ByteBuf byteBuf2 = writeFullServiceURL(serviceURL);
        byteBuf1.writeBytes(byteBuf2);
        return byteBuf1;
    }

    /**
     * 创建不全的数据报文
     *
     * @param serviceURL 服务
     * @return ByteBuf
     * @throws Exception
     */
    private ByteBuf writeUnpackingServiceURL(ServiceURL serviceURL) throws Exception {
        //堆上创建或者堆外创建
        ByteBuf directBuffer = Unpooled.directBuffer();
        serviceURL.setProtocolType(ProtocolEnum.MOJITO.getType());
        serviceURL.setSerializationType(SerializeEnum.HESSION2.getType());
        Serialize serialize = SerializeEnum.ofByType(serviceURL.getSerializationType()).getSerialize().newInstance();
        byte[] serializeData = serialize.serialize(serviceURL);
        //协议头
        directBuffer.writeByte(serviceURL.getProtocolType());
        //序列化类型
        directBuffer.writeByte(serviceURL.getSerializationType());
        //数据长度
        directBuffer.writeInt(serializeData.length);
        //数据信息
//        directBuffer.writeBytes(serializeData);
        return directBuffer;
    }

    private void fillWriteUnpackingServiceURL(ByteBuf unpackingByteBuf, ServiceURL serviceURL) throws Exception {
        Serialize serialize = SerializeEnum.ofByType(serviceURL.getSerializationType()).getSerialize().newInstance();
        byte[] serializeData = serialize.serialize(serviceURL);
        //重新填充报文
        unpackingByteBuf.writeBytes(serializeData);
    }

    /**
     * 拆包场景的解析
     * 1. 第一次网络数据还没有全部写入到socket中所以当做拆包处理，不解析。继续等待数据到来在解析
     * 2. 第二次发现数据已经完整了,此时就处理
     *
     * @throws Exception 异常
     */
    public void testUnpackingServiceURLDecoder(ByteBuf unpackingByteBuf, ServiceURL serviceURL) throws Exception {
        System.out.println("可读的长度:" + unpackingByteBuf.readableBytes());
        System.out.println(unpackingByteBuf);
        EmbeddedChannel channel = new EmbeddedChannel(new MojitoChannelDecoder("serverChannelHandler"), new MojitoChannelEncoder("serverChannelHandler"));
        channel.writeInbound(unpackingByteBuf.retain());
        //入栈对象
        System.out.println("第一次数据不足:" + channel.readInbound());
        //对于拆包情况这种肯定是读取不了的，所以在这里重新将包填充满
        fillWriteUnpackingServiceURL(unpackingByteBuf, serviceURL);
        channel.writeInbound(unpackingByteBuf.retain());
        System.out.println("第二次网络数据填充完整了就有值了:" + channel.readInbound());
        channel.finish();
    }

    /**
     * 正常解析
     *
     * @throws Exception 异常
     */
    public void testServiceURLDecoder(ByteBuf buffer) throws Exception {
        System.out.println("可读的长度:" + buffer.readableBytes());
        System.out.println(buffer);
        EmbeddedChannel channel = new EmbeddedChannel(new MojitoChannelDecoder("serverChannelHandler"), new MojitoChannelEncoder("serverChannelHandler"));
        channel.writeInbound(buffer.retain());
        //入栈对象
        Queue<Object> objects = channel.inboundMessages();
        System.out.println("解析到的数据个数:" + objects.size());
        for (Object object : objects) {
            System.out.println(object);
        }
        channel.finish();
    }

    @Test
    public void test() {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setProtocolType(ProtocolEnum.MOJITO.getType());
        rpcRequest.setSerializationType(SerializeEnum.PROTOSTUFF.getType());
        rpcRequest.setArgs(new Object[]{new User()});

        //1. 设置请求服务
        rpcRequest.setServiceType(UserService.class);
        //2. 设置请求方法
        rpcRequest.setMethodName("addUserAge");
        Method addUserAge = MethodTools.findMethodByName(UserService.class, "addUserAge", User.class);
        Parameter[] parameters = addUserAge.getParameters();
        List<MethodParameter> methodParameters = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            methodParameters.add(MethodParameter.forMethodOrConstructor(addUserAge, i));
        }
        System.out.println(methodParameters);
//        rpcRequest.setArgsType();
        rpcRequest.setArgs(new Object[]{new User()});
        //3. 设置返回值类型
        rpcRequest.setReturnType(User.class);
        //4. 设置请求耗时
        rpcRequest.setTimeout(100);

        //消息的传递通过url字符串来完成
        //1. 方法签名生成url
        //2. url反生成方法签名
    }

}
