package com.hanframework.mojito.signature.service;

import com.hanframework.mojito.signature.method.MethodSignature;

import java.util.List;

/**
 * @author liuxin
 * 2020-07-25 21:29
 */
public interface ServiceSignature<T> {

    /**
     * 获取服务实例
     *
     * @return 获取服务实例
     */
    T getRef();

    /**
     * 获取服务版本
     *
     * @return String
     */
    String getVersion();


    /**
     * 服务类型
     *
     * @return Class
     */
    Class<?> getServiceType();

    /**
     * 导出服务路径
     *
     * @return ServiceURL
     */
    List<MethodSignature> getMethodSignature();
}
