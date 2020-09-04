package com.hanframework.mojito.serialization;

import com.hanframework.mojito.exception.DeserializeException;
import com.hanframework.mojito.exception.SerializeException;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;


/**
 * 注意:
 *
 * @author liuxin
 * 2020-07-31 21:32
 */
public class ProtostuffObjectSerializer implements Serializer {

    @Override
    public byte[] serialize(Object dataObject) throws SerializeException {
        Schema schema = RuntimeSchema.getSchema(dataObject.getClass());
        return ProtostuffIOUtil.toByteArray(dataObject, schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    @Override
    public Object deserialize(byte[] data) throws DeserializeException {
        return deserialize(data, Object.class);

    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> dataType) throws DeserializeException {
        T t = null;
        try {
            t = dataType.newInstance();
            Schema schema = RuntimeSchema.getSchema(t.getClass());
            if (schema == null) {
                schema = RuntimeSchema.createFrom(dataType.getClass());
            }
            ProtostuffIOUtil.mergeFrom(data, t, schema);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DeserializeException(e);
        }
        return t;
    }
}
