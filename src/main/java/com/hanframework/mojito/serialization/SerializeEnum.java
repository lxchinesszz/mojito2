package com.hanframework.mojito.serialization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 性能优先级
 * 1. ProtostuffObjectSerializer
 * 2. NettyCompactObjectSerializer
 * 3. Hession2ObjectSerializer
 * 4. NettyObjectSerializer
 * 5. HessionObjectSerializer
 */

public enum SerializeEnum {

    PROTOSTUFF(1, ProtostuffObjectSerializer.class),

    COMPACT(2, NettyCompactObjectSerializer.class),

    HESSION2(3, Hession2ObjectSerializer.class),

    SIMPLE(4, NettyObjectSerializer.class),

    HESSION(5, HessionObjectSerializer.class),
    ;

    private byte type;

    private Class<? extends Serializer> serialize;

    SerializeEnum(int type, Class<? extends Serializer> serialize) {
        this.type = (byte) type;
        this.serialize = serialize;
    }

    public byte getType() {
        return type;
    }

    public Class<? extends Serializer> getSerialize() {
        return serialize;
    }

    private static final Map<Byte, SerializeEnum> cacheMap = new ConcurrentHashMap<>(values().length);

    static {
        for (SerializeEnum serializeEnum : values()) {
            cacheMap.put(serializeEnum.type, serializeEnum);
        }
    }

    public static SerializeEnum ofByType(Byte type) {
        return cacheMap.getOrDefault(type, PROTOSTUFF);
    }
}
