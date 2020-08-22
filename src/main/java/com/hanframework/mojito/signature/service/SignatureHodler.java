package com.hanframework.mojito.signature.service;

import com.hanframework.mojito.signature.method.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author liuxin
 * 2020-08-01 19:14
 */
public class SignatureHodler {

    private ServiceSignature serviceSignature;

    private MethodSignature methodSignature;

    public Object invoker(Object ref, Object[] args) throws Exception {
        Method method = methodSignature.getMethod();
        return method.invoke(ref, args);
    }

    public Object invoker(Object[] args) throws Exception {
        Method method = methodSignature.getMethod();
        return method.invoke(serviceSignature.getRef(), args);
    }
}
