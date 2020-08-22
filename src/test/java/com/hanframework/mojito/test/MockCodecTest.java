package com.hanframework.mojito.test;

import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;

/**
 * 1. 如何自定义数据模型
 *
 * @author liuxin
 * 2020-08-22 16:43
 */
public class MockCodecTest implements Serializable {

    class CustomerRpcInfo extends RpcProtocolHeader {
        private String name;

        public CustomerRpcInfo(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "CustomerRpcInfo{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    /**
     * 1. 正常数据（非拆包和粘包数据数据）
     */
    @Test
    public void customModelTest() throws Exception {
        //1. 客户端将数据模型,转换成二进制数据进行网络通信
        CustomerRpcInfo customerRpcInfo = new CustomerRpcInfo("自定义数据模型");
        ByteBuf encode = MockCodec.encode(customerRpcInfo);

        //2. 服务端解析客户端的二进制请求信息(数据可能出现粘包和拆包问题,拆包数据会等待数据到齐然后处理,粘包会自动拆粘包数据返回List)
        List<? extends RpcProtocolHeader> decodes = MockCodec.decode(encode);
        for (RpcProtocolHeader decode : decodes) {
            System.out.println("解析到的数据:" + decode);
        }
    }

    /**
     * 2. 粘包数据
     */
    @Test
    public void stickPackageTest() throws Exception {
        CustomerRpcInfo customerRpcInfoOne = new CustomerRpcInfo("自定义数据模型-1");
        ByteBuf packageOne = MockCodec.encode(customerRpcInfoOne);

        //1. 客户端将数据模型,转换成二进制数据进行网络通信
        CustomerRpcInfo customerRpcInfoTwo = new CustomerRpcInfo("自定义数据模型-2");
        ByteBuf packageTwo = MockCodec.encode(customerRpcInfoTwo);

        //2. 将报文合并模拟粘包数据
        ByteBuf combinePackage = MockCodec.merge(packageOne, packageTwo);
        List<? extends RpcProtocolHeader> decodes = MockCodec.decode(combinePackage);
        for (RpcProtocolHeader decode : decodes) {
            System.out.println("解析到的数据:" + decode);
        }
    }

    /**
     * 3. 拆包数据
     *
     * @throws Exception
     */
    @Test
    public void unpackingTest() throws Exception {
        CustomerRpcInfo customerRpcInfoOne = new CustomerRpcInfo("自定义数据模型-1");
        ByteBuf encode = MockCodec.encode(customerRpcInfoOne);
        System.out.println("总长度:" + encode.readableBytes());

        //1. 将数据拆分
        List<ByteBuf> unpacking = MockCodec.unpacking(encode);
        for (ByteBuf byteBuf : unpacking) {
            System.out.println("分段长度:" + byteBuf.readableBytes());
        }

        //2. 睡眠100毫秒之后将数据补充完整
        Runnable runnable = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            unpacking.get(0).writeBytes(unpacking.get(1));
        };
        new Thread(runnable).start();

        //3. 循环等待数据
        List<? extends RpcProtocolHeader> decode = MockCodec.decode(unpacking.get(0));
        System.out.println(decode);

    }
}