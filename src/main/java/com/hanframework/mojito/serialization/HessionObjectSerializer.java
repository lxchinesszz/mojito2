package com.hanframework.mojito.serialization;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
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
public class HessionObjectSerializer implements Serializer {

    @Override
    public byte[] serialize(Object dataObject) throws SerializeException {
        ByteArrayOutputStream dataArr = new ByteArrayOutputStream();
        HessianOutput oeo = null;
        try {
            oeo = new HessianOutput(dataArr);
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
        HessianInput odi = new HessianInput(new ByteArrayInputStream(data));
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
