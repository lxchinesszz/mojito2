package com.hanframework.mojito.signature.service;


/**
 * 单例方式获取实例
 *
 * @author liuxin
 * 2020-07-25 21:23
 */
public class SingletonServiceServiceSignature<T> extends AbstractServiceSignature {

    public SingletonServiceServiceSignature(Class<T> serviceClass, String version, T ref) {
        super(serviceClass, version, ref);
    }
}
