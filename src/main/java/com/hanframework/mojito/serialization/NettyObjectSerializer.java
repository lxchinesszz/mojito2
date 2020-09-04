package com.hanframework.mojito.serialization;

import com.hanframework.mojito.exception.DeserializeException;
import com.hanframework.mojito.exception.SerializeException;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;

/**
 * 必须要实现Serializable接口
 *
 * @author liuxin
 * 2020-07-31 20:16
 */
public class NettyObjectSerializer implements Serializer {

    @Override
    public byte[] serialize(Object dataObject) {
        ByteArrayOutputStream dataArr = new ByteArrayOutputStream();
        try (ObjectEncoderOutputStream oeo = new ObjectEncoderOutputStream(dataArr)) {
            oeo.writeObject(dataObject);
            oeo.flush();
        } catch (IOException e) {
            if (e instanceof NotSerializableException) {
                throw new SerializeException(e.getMessage() + ",未实现Serializable接口", e);
            } else {
                throw new SerializeException(e);
            }
        }
        return dataArr.toByteArray();
    }

    @Override
    public Object deserialize(byte[] data) throws DeserializeException {
        Object o;
        try(ObjectDecoderInputStream odi = new ObjectDecoderInputStream(new ByteArrayInputStream(data))){
            o = odi.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new DeserializeException(e);
        }
        return o;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> dataType) throws DeserializeException {
        return ((T) deserialize(data));
    }
}
