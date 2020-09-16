package com.hanframework.mojito.protocol.mojito.model;

import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.serialization.SerializeEnum;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    String id;

    private final Map<String, String> attachments = new ConcurrentHashMap<>();


    public String getAttachment(String key) {
        return (String) this.attachments.get(key);
    }

    public void setAttachment(String key, String value) {
        if (value == null) {
            this.attachments.remove(key);
        } else {
            this.attachments.put(key, value);
        }
    }

    public void removeAttachment(String key) {
        this.attachments.remove(key);
    }

    public Map<String, String> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Map<String, String> attachment) {
        if (attachment != null && attachment.size() > 0) {
            this.attachments.putAll(attachment);
        }
    }

    public void clearAttachments() {
        this.attachments.clear();
    }

    public int getType() {
        return type;
    }

    public void setType(int requestType) {
        this.type = requestType;
    }

    public RpcProtocolHeader() {
        id = String.valueOf(INVOKE_ID.incrementAndGet());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
