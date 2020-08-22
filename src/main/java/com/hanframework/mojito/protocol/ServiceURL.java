package com.hanframework.mojito.protocol;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author liuxin
 * 2020-07-31 22:43
 */
public class ServiceURL implements Serializable {


    private byte protocolType;


    private byte serializationType;


    private String serviceType;

    /**
     * 请求方法
     */
    private String methodName;

    /**
     * 参数类型
     */
    private String[] argsType;

    /**
     * 返回值类型
     */
    private String returnType;

    /**
     * 请求参数
     */
    private Object[] args;

    /**
     * 超时时间
     */
    private long timeout;

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public byte getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(byte serializationType) {
        this.serializationType = serializationType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getArgsType() {
        return argsType;
    }

    public void setArgsType(String[] argsType) {
        this.argsType = argsType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "ServiceURL{" +
                "protocolType=" + protocolType +
                ", serializationType=" + serializationType +
                ", serviceType='" + serviceType + '\'' +
                ", methodName='" + methodName + '\'' +
                ", argsType=" + Arrays.toString(argsType) +
                ", returnType='" + returnType + '\'' +
                ", args=" + Arrays.toString(args) +
                ", timeout=" + timeout +
                '}';
    }
}
