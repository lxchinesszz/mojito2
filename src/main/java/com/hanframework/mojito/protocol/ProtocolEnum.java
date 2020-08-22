package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.mojito.MojitoProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 性能优先级
 * 1. ProtostuffObjectSerialize
 * 2. NettyCompactObjectSerialize
 * 3. Hession2ObjectSerialize
 * 4. NettyObjectSerialize
 * 5. HessionObjectSerialize
 */

public enum ProtocolEnum {

    /**
     * 循环协议
     */
    ECHO(1, null),
    MOJITO(2, MojitoProtocol.class);

    private byte type;

    private Class<? extends Protocol> protocol;

    private static Map<Byte, ProtocolEnum> cache = new HashMap<>();

    static {
        for (ProtocolEnum protocolEnum : values()) {
            cache.put(protocolEnum.type, protocolEnum);
        }
    }

    public static ProtocolEnum byType(byte type) {
        return cache.get(type);
    }

    ProtocolEnum(int type, Class<? extends Protocol> protocol) {
        this.type = (byte) type;
        this.protocol = protocol;
    }

    public byte getType() {
        return type;
    }

    public Class<? extends Protocol> getProtocol() {
        return protocol;
    }
}
