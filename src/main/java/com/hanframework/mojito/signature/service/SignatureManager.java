package com.hanframework.mojito.signature.service;

import com.hanframework.mojito.protocol.mojito.model.RpcRequest;

/**
 * @author liuxin
 * 2020-07-31 17:44
 */
public interface SignatureManager {

    /**
     * 根据请求找到签名,将签名和请求信息给处理器
     *
     * @param rpcRequest rpc请求
     * @return ServiceSignature
     */
    SignatureHodler fetchSignatureHodler(RpcRequest rpcRequest);


}
