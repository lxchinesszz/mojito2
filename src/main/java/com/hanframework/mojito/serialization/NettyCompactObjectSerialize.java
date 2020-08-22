package com.hanframework.mojito.serialization;

import com.hanframework.mojito.exception.DeserializeException;
import com.hanframework.mojito.exception.SerializeException;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;

import java.io.*;

/**
 * @author liuxin
 * 2020-07-31 21:10
 */
public class NettyCompactObjectSerialize implements Serialize {

    @Override
    public byte[] serialize(Object dataObject) {
        ByteArrayOutputStream dataArr = new ByteArrayOutputStream();
        try (CompactObjectOutputStream oeo = new CompactObjectOutputStream(dataArr)) {
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
        try (CompactObjectInputStream odi = new CompactObjectInputStream(new ByteArrayInputStream(data), ClassResolvers.cacheDisabled(null))) {
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


    private class CompactObjectOutputStream extends ObjectOutputStream {

        static final int TYPE_FAT_DESCRIPTOR = 0;
        static final int TYPE_THIN_DESCRIPTOR = 1;

        CompactObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            writeByte(STREAM_VERSION);
        }

        @Override
        protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
            Class<?> clazz = desc.forClass();
            if (clazz.isPrimitive() || clazz.isArray() || clazz.isInterface() ||
                    desc.getSerialVersionUID() == 0) {
                write(TYPE_FAT_DESCRIPTOR);
                super.writeClassDescriptor(desc);
            } else {
                write(TYPE_THIN_DESCRIPTOR);
                writeUTF(desc.getName());
            }
        }
    }

    /**
     * 压缩
     */
    private class CompactObjectInputStream extends ObjectInputStream {

        static final int TYPE_FAT_DESCRIPTOR = 0;

        static final int TYPE_THIN_DESCRIPTOR = 1;

        private final ClassResolver classResolver;

        CompactObjectInputStream(InputStream in, ClassResolver classResolver) throws IOException {
            super(in);
            this.classResolver = classResolver;
        }

        @Override
        protected void readStreamHeader() throws IOException {
            int version = readByte() & 0xFF;
            if (version != STREAM_VERSION) {
                throw new StreamCorruptedException(
                        "Unsupported version: " + version);
            }
        }

        @Override
        protected ObjectStreamClass readClassDescriptor()
                throws IOException, ClassNotFoundException {
            int type = read();
            if (type < 0) {
                throw new EOFException();
            }
            switch (type) {
                case TYPE_FAT_DESCRIPTOR:
                    return super.readClassDescriptor();
                case TYPE_THIN_DESCRIPTOR:
                    String className = readUTF();
                    Class<?> clazz = classResolver.resolve(className);
                    return ObjectStreamClass.lookupAny(clazz);
                default:
                    throw new StreamCorruptedException(
                            "Unexpected class descriptor type: " + type);
            }
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            Class<?> clazz;
            try {
                clazz = classResolver.resolve(desc.getName());
            } catch (ClassNotFoundException ignored) {
                clazz = super.resolveClass(desc);
            }

            return clazz;
        }
    }
}
