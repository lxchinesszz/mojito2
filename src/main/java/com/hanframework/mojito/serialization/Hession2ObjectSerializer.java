package com.hanframework.mojito.serialization;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.hanframework.mojito.exception.DeserializeException;
import com.hanframework.mojito.exception.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;

/**
 * @author liuxin
 * 2020-07-31 21:32
 */
public class Hession2ObjectSerializer implements Serializer {

    @Override
    public byte[] serialize(Object dataObject) throws SerializeException {
        ByteArrayOutputStream dataArr = new ByteArrayOutputStream();
        Hessian2Output oeo = null;
        try {
            oeo = new Hessian2Output(dataArr);
            oeo.writeObject(dataObject);
            oeo.flush();
        } catch (IOException e) {
            if (e instanceof NotSerializableException) {
                throw new SerializeException(e.getMessage() + ",未实现Serializable接口", e);
            } else {
                throw new SerializeException(e);
            }
        } finally {
            if (oeo != null) {
                try {
                    oeo.close();
                } catch (IOException e) {
                    throw new SerializeException(e);
                }
            }
        }
        return dataArr.toByteArray();
    }

    @Override
    public Object deserialize(byte[] data) throws DeserializeException {
        Object o;
        Hessian2Input odi = new Hessian2Input(new ByteArrayInputStream(data));
        try {
            o = odi.readObject();
        } catch (IOException e) {
            throw new DeserializeException(e);
        }
        return o;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> dataType) throws DeserializeException {
        return ((T) deserialize(data));
    }
}
