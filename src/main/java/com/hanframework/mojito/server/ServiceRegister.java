package com.hanframework.mojito.server;

import com.hanframework.mojito.signature.service.SignatureManager;

/**
 * 注册接口
 * 将某接口注册到服务端上
 *
 * @author liuxin
 * 2020-07-25 21:16
 */
public interface ServiceRegister {

    /**
     * 注册服务
     *
     * @param signatureManager 需要发布的服务管理器
     */
    void registerService(SignatureManager signatureManager);

    /**
     * 获取签名管理器
     *
     * @return SignatureManager
     */
    SignatureManager getSignatureManager();
}
