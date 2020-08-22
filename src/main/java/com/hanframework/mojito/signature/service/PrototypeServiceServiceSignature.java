package com.hanframework.mojito.signature.service;


import java.util.function.Supplier;

/**
 * 原型方式获取实例
 *
 * @author liuxin
 * 2020-07-25 21:23
 */
public class PrototypeServiceServiceSignature<T> extends AbstractServiceSignature<T> {

    private Supplier<T> supplierRef;

    public PrototypeServiceServiceSignature(Class<T> serviceClass, String version, Supplier<T> supplierRef) {
        super(serviceClass, version, supplierRef.get());
        this.supplierRef = supplierRef;
    }

    @Override
    public T getRef() {
        return supplierRef.get();
    }
}
