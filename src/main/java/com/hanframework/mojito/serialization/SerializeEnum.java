package com.hanframework.mojito.serialization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 性能优先级
 * 1. ProtostuffObjectSerialize
 * 2. NettyCompactObjectSerialize
 * 3. Hession2ObjectSerialize
 * 4. NettyObjectSerialize
 * 5. HessionObjectSerialize
 */

public enum SerializeEnum {

    PROTOSTUFF(1, ProtostuffObjectSerialize.class),

    COMPACT(2, NettyCompactObjectSerialize.class),

    HESSION2(3, Hession2ObjectSerialize.class),

    SIMPLE(4, NettyObjectSerialize.class),

    HESSION(5, HessionObjectSerialize.class),
    ;

    private byte type;

    private Class<? extends Serialize> serialize;

    SerializeEnum(int type, Class<? extends Serialize> serialize) {
        this.type = (byte) type;
        this.serialize = serialize;
    }

    public byte getType() {
        return type;
    }

    public Class<? extends Serialize> getSerialize() {
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
