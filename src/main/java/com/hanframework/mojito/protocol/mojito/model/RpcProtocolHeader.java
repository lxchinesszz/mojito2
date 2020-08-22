package com.hanframework.mojito.protocol.mojito.model;

import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.serialization.SerializeEnum;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author liuxin
 * 2020-07-31 21:49
 */
public class RpcProtocolHeader implements Serializable {

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    /**
     * 主协议
     *
     * @see ProtocolEnum
     */
    byte protocolType = ProtocolEnum.MOJITO.getType();

    /**
     * 序列化协议
     *
     * @see SerializeEnum
     */
    byte serializationType = SerializeEnum.HESSION2.getType();

    /**
     * 请求类型
     *
     * @see NetType
     */
    int type = NetType.SERVICE.getType();

    /**
     * 唯一id
     */
    Long id;

    public int getType() {
        return type;
    }

    public void setType(int requestType) {
        this.type = requestType;
    }

    public RpcProtocolHeader() {
        id = INVOKE_ID.incrementAndGet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public byte getSerializationType() {
        return serializationType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public void setSerializationType(byte serializationType) {
        this.serializationType = serializationType;
    }
}
