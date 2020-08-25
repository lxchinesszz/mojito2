package com.hanframework.mojito.protocol.mojito.model;

import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.serialization.SerializeEnum;

/**
 * @author liuxin
 * 2020-08-01 19:40
 */
public class RpcTools {


    public static RpcProtocolHeader fetchRpcProtocolHeader(RpcRequest rpcRequest) {
        return rpcRequest;
    }

    public static RpcProtocolHeader fetchRpcProtocolHeader(RpcResponse rpcResponse) {
        return rpcResponse;
    }

    public static RpcResponse buildRpcResponse(RpcRequest rpcRequest, Object result) {
        RpcProtocolHeader rpcProtocolHeader = fetchRpcProtocolHeader(rpcRequest);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setId(rpcRequest.getId());
        rpcResponse.setProtocolType(rpcProtocolHeader.protocolType);
        rpcResponse.setSerializationType(rpcProtocolHeader.serializationType);
        rpcResponse.setServiceType(rpcRequest.getServiceType());
        rpcResponse.setArgsType(rpcRequest.getArgsType());
        rpcResponse.setResult(result);
        return rpcResponse;
    }

    private static RpcResponse buildErrorRpcResponse(RpcRequest rpcRequest, String message) {
        RpcProtocolHeader rpcProtocolHeader = fetchRpcProtocolHeader(rpcRequest);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setProtocolType(rpcProtocolHeader.protocolType);
        rpcResponse.setSerializationType(rpcProtocolHeader.serializationType);
        rpcResponse.setServiceType(rpcRequest.getServiceType());
        rpcResponse.setArgsType(rpcRequest.getArgsType());
        rpcResponse.setSuccess(false);
        rpcResponse.setId(rpcRequest.getId());
        return rpcResponse;
    }

    public static RpcResponse buildNotFoundService(RpcRequest rpcRequest) {
        return buildErrorRpcResponse(rpcRequest, "未找到服务" + rpcRequest.getServiceType() + "version:" + rpcRequest.getVersion());
    }

    public static RpcRequest buildHeartBeatRequest() {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setProtocolType(ProtocolEnum.MOJITO.getType());
        rpcRequest.setSerializationType(SerializeEnum.HESSION2.getType());
        rpcRequest.setType(NetType.HEARTBEAT.getType());
        return rpcRequest;
    }

    public static RpcResponse buildHeartBeatResponse(RpcRequest rpcRequest) {
        RpcResponse response = buildRpcResponse(rpcRequest, null);
        response.setSuccess(false);
        response.setType(NetType.HEARTBEAT.getType());
        response.setId(rpcRequest.getId());
        return response;
    }
}
