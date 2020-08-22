package com.hanframework.mojito.serialization;

import com.hanframework.kit.text.UnixColor;
import com.hanframework.mojito.protocol.ServiceURL;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.test.pojo.TestSerialize;
import com.hanframework.mojito.test.pojo.User;
import org.junit.Test;

import java.io.Serializable;


/**
 * @author liuxin
 * 2020-07-31 20:31
 */
public class SerializeTest implements Serializable {

    /**
     * 性能优先级
     * 1. ProtostuffObjectSerialize
     * 2. NettyCompactObjectSerialize
     * 3. Hession2ObjectSerialize
     * 4. NettyObjectSerialize
     * 5. HessionObjectSerialize
     */
    @Test
    public void testSerialize() {
        User user = new User();

        //指定要反序列化的类型
        System.out.println("指定要反序列化的类型");
        printInfo(new NettyObjectSerialize(), user);

        printInfo(new NettyCompactObjectSerialize(), user);

        printInfo(new HessionObjectSerialize(), user);

        printInfo(new Hession2ObjectSerialize(), user);

        printInfo(new ProtostuffObjectSerialize(), user);


        //不指定要反序列化的类型
        System.out.println("不指定要反序列化的类型");
        printInfoNotClass(new NettyObjectSerialize(), user);

        printInfoNotClass(new NettyCompactObjectSerialize(), user);

        printInfoNotClass(new HessionObjectSerialize(), user);

        printInfoNotClass(new Hession2ObjectSerialize(), user);

        printInfoNotClass(new ProtostuffObjectSerialize(), user);
    }

    @Test
    public void test() {
        Hession2ObjectSerialize hession2ObjectSerialize = new Hession2ObjectSerialize();
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setProtocolType((byte) 1);
        rpcRequest.setSerializationType((byte) 2);
        rpcRequest.setMethodName("dfsfd");
        byte[] serialize = hession2ObjectSerialize.serialize(rpcRequest);
        byte[] serialize1 = hession2ObjectSerialize.serialize(((RpcProtocolHeader) rpcRequest));
        System.out.println(serialize.length == serialize1.length);
    }

    /**
     * ProtostuffObjectSerialize 反序列化必须要知道类型。
     */
    @Test
    public void testProtostuffObjectSerialize() {
        ProtostuffObjectSerialize serialize = new ProtostuffObjectSerialize();
        User testSerialize = new User();
        byte[] bytes = serialize.serialize(testSerialize);
        Object deserialize = serialize.deserialize(bytes);
        System.out.println(deserialize);
    }


    private synchronized void printInfo(Serialize serializetor, Object obj) {
        byte[] serialize = serializetor.serialize(obj);
        Object deserialize = serializetor.deserialize(serialize, obj.getClass());
        System.out.println(deserialize);
        UnixColor unixColor = new UnixColor();
        unixColor.green("长度:" + serialize.length);
        if (serialize.length > 0) {
            for (int i = 0; i < serialize.length; i++) {
                System.out.print("line[" + i + "]=" + serialize[i] + ",");
            }
        }
        System.out.println("-------------");
    }


    private synchronized void printInfoNotClass(Serialize serializetor, Object obj) {
        byte[] serialize = serializetor.serialize(obj);
        Object deserialize = serializetor.deserialize(serialize);
        System.out.println(deserialize);
        UnixColor unixColor = new UnixColor();
        unixColor.green("长度:" + serialize.length);
        if (serialize.length > 0) {
            for (int i = 0; i < serialize.length; i++) {
                System.out.print("line[" + i + "]=" + serialize[i] + ",");
            }
        }
        System.out.println("-------------");
    }


}