package com.hanframework.mojito.serialization;

import com.hanframework.mojito.exception.DeserializeException;

/**
 * @author liuxin
 * 2020-07-31 20:13
 */
public interface Serializer {


    /**
     * 负责将对象转换成二进制数据
     *
     * @param dataObject 数据对象
     * @return byte[]
     */
    byte[] serialize(Object dataObject);

    /**
     * 将二进制数据反序列化成Java对象
     *
     * @param data 二进制数据
     * @return Object
     * @throws DeserializeException 反序列化异常
     */
    Object deserialize(byte[] data) throws DeserializeException;

    /**
     * 将二进制数据反序列化成Java对象
     *
     * @param data     二进制数据
     * @param dataType 数据类型
     * @return Object
     * @throws DeserializeException 反序列化异常
     */
    <T> T deserialize(byte[] data, Class<T> dataType) throws DeserializeException;
}
