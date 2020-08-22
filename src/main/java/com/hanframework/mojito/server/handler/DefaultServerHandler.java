package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.protocol.mojito.model.RpcTools;
import com.hanframework.mojito.signature.service.SignatureHodler;

import java.util.Objects;

/**
 * @author liuxin
 * 2020-08-01 20:04
 */
public class DefaultServerHandler extends AbstractServerHandler<RpcRequest, RpcResponse> {


    public RpcResponse doHandler(RpcRequest rpcRequest) throws Exception {
//        if (Objects.isNull(null)) {
        return RpcTools.buildNotFoundService(rpcRequest);
//        }
//        Object result = signatureHodler.invoker(rpcRequest.getArgs());
//        return RpcTools.buildRpcResponse(rpcRequest, result);
    }

}
