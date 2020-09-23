package com.hanframework.mojito.serialization;

import com.hanframework.kit.text.UnixColor;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.test.pojo.User;
import org.junit.Test;

import java.io.Serializable;


/**
 * @author liuxin
 * 2020-07-31 20:31
 */
public class SerializerTest implements Serializable {

    /**
     * 性能优先级
     * 1. ProtostuffObjectSerializer
     * 2. NettyCompactObjectSerializer
     * 3. Hession2ObjectSerializer
     * 4. NettyObjectSerializer
     * 5. HessionObjectSerializer
     */
    @Test
    public void testSerialize() {
        User user = new User();

        //指定要反序列化的类型
        System.out.println("指定要反序列化的类型");
        printInfoAccordingToType(new NettyObjectSerializer(), user);

        printInfoAccordingToType(new NettyCompactObjectSerializer(), user);

        printInfoAccordingToType(new HessionObjectSerializer(), user);

        printInfoAccordingToType(new Hession2ObjectSerializer(), user);

        printInfoAccordingToType(new ProtostuffObjectSerializer(), user);


        //不指定要反序列化的类型
        System.out.println("不指定要反序列化的类型");
        printInfoNotAccordingToType(new NettyObjectSerializer(), user);

        printInfoNotAccordingToType(new NettyCompactObjectSerializer(), user);

        printInfoNotAccordingToType(new HessionObjectSerializer(), user);

        printInfoNotAccordingToType(new Hession2ObjectSerializer(), user);

        //FIXME protostuff 如果不指定类型就会报错
        printInfoNotAccordingToType(new ProtostuffObjectSerializer(), user);
    }

    @Test
    public void test() {
        Hession2ObjectSerializer hession2ObjectSerialize = new Hession2ObjectSerializer();
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setProtocolType((byte) 1);
        rpcRequest.setSerializationType((byte) 2);
        rpcRequest.setMethodName("dfsfd");
        byte[] serialize = hession2ObjectSerialize.serialize(rpcRequest);
        byte[] serialize1 = hession2ObjectSerialize.serialize(((RpcProtocolHeader) rpcRequest));
        System.out.println(serialize.length == serialize1.length);
    }

    /**
     * ProtostuffObjectSerializer 反序列化必须要知道类型。
     */
    @Test(expected = NullPointerException.class)
    public void testProtostuffObjectSerialize() {
        ProtostuffObjectSerializer serialize = new ProtostuffObjectSerializer();
        User testSerialize = new User();
        byte[] bytes = serialize.serialize(testSerialize);
        Object deserialize = serialize.deserialize(bytes);
        System.out.println(deserialize);
    }


    private synchronized void printInfoAccordingToType(Serializer serializetor, Object obj) {
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


    private synchronized void printInfoNotAccordingToType(Serializer serializetor, Object obj) {
        byte[] serialize = serializetor.serialize(obj);
        Object deserialize = serializetor.deserialize(serialize,obj.getClass());
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