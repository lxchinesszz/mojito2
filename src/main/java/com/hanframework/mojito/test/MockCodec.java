package com.hanframework.mojito.test;

import com.hanframework.mojito.exception.ProtocolException;
import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.protocol.mojito.MojitoChannelDecoder;
import com.hanframework.mojito.protocol.mojito.MojitoChannelEncoder;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liuxin
 * 2020-08-22 16:28
 */
public abstract class MockCodec {

    private static void checked(RpcProtocolHeader rpcProtocolHeader) {
        byte protocolType = rpcProtocolHeader.getProtocolType();
        if (Objects.isNull(ProtocolEnum.byType(protocolType))) {
            StringBuilder error = new StringBuilder();
            error.append("不支持的协议类型,请指定协议类型").append("\n\t");
            ProtocolEnum[] protocolEnums = ProtocolEnum.values();
            for (ProtocolEnum protocolEnum : protocolEnums) {
                error.append("支持的协议类型:" + protocolEnum.getType() + ",协议处理类:" + protocolEnum.getProtocol()).append("\n\t");
            }
            throw new ProtocolException(error.toString());
        }
    }

    /**
     * 主要依据:
     * 数据有
     * 协议类型(1位byte) + 序列化类型(1位byte) + 报文大小(4位int) + 数据报文组成(N位byte[])
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
    public static ByteBuf encode(RpcProtocolHeader rpcProtocolHeader) throws Exception {
        checked(rpcProtocolHeader);
        MojitoChannelEncoder mojitoChannelEncoder = new MojitoChannelEncoder("mock");
        ByteBuf out = Unpooled.buffer();
        mojitoChannelEncoder.encode(null, rpcProtocolHeader, out);
        out.markReaderIndex();
        return out;
    }

    /**
     * 模拟粘包数据
     *
     * @param packages 粘包数据组
     * @return ByteBuf
     */
    public static ByteBuf merge(ByteBuf... packages) {
        ByteBuf byteBuf = Unpooled.buffer();
        for (ByteBuf aPackage : packages) {
            byteBuf.writeBytes(aPackage);
        }
        return byteBuf;
    }

    public static List<? extends RpcProtocolHeader> decode(ByteBuf byteBuf) throws Exception {
        byteBuf.resetReaderIndex();
        MojitoChannelDecoder mojitoChannelDecoder = new MojitoChannelDecoder("mock");
        List<Object> out = new ArrayList<>();
        while (byteBuf.isReadable()) {
            mojitoChannelDecoder.decode(null, byteBuf, out);
        }
        return out.stream().map(o -> (RpcProtocolHeader) o).collect(Collectors.toList());
    }

    private static ByteBuf readBytes(ByteBuf byteBuf, int startIndex, int endIndex) {
        byte[] data = new byte[endIndex - startIndex];
        byteBuf.readBytes(data, startIndex, endIndex);
        ByteBuf buf = Unpooled.buffer();
        return buf.writeBytes(data);
    }



    /**
     * 拆包数据
     *
     * @param byteBuf 合并数据
     * @return List
     */
    public static List<ByteBuf> unpacking(ByteBuf byteBuf) {
        List<ByteBuf> merged = new ArrayList<>();
        //1. 总长度
        int readableBytes = byteBuf.readableBytes();
        merged.add(readBytes(byteBuf, 0, readableBytes / 2));
        System.out.println(byteBuf.readableBytes());
        merged.add(byteBuf);
        return merged;
    }
}
