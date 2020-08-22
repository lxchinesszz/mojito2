package com.hanframework.mojito.signature.service;

import java.util.function.Supplier;

/**
 * 提供更简单的构建签名的工具类
 *
 * @author liuxin
 * 2020-07-31 16:59
 */
public final class SignatureBuilder {


    public static <T> SingletonServiceServiceSignature<T> buildSingletonSignature(Class<T> serviceClass, String version, T ref) {
        return new SingletonServiceServiceSignature<>(serviceClass, version, ref);
    }

    public static <T> PrototypeServiceServiceSignature<T> buildPrototypeServiceSignature(Class<T> serviceClass, String version, Supplier<T> supplierRef) {
        return new PrototypeServiceServiceSignature<>(serviceClass, version, supplierRef);
    }
}
