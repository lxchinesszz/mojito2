package com.hanframework.mojito.protocol.mojito.model;

/**
 * @author liuxin
 * 2020-08-02 01:21
 */
public enum NetType {
    HEARTBEAT(0, "心跳请求"),
    SERVICE(1, "服务请求");
    private int type;

    private String desc;

    NetType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
