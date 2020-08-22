package com.hanframework.mojito.signature.service;

import com.hanframework.mojito.signature.method.MethodSignature;
import com.hanframework.mojito.signature.method.SimpleMethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法签名
 *
 * @author liuxin
 * 2020-07-23 23:14
 */
public class AbstractServiceSignature<T> implements ServiceSignature<T> {

    /**
     * 服务类字节码
     */
    private Class<T> serviceClass;

    /**
     * 版本
     */
    private String version;

    /**
     * 服务类实例
     */
    private T ref;

    protected AbstractServiceSignature(Class<T> serviceClass, String version, T ref) {
        this.serviceClass = serviceClass;
        this.version = version;
        this.ref = ref;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public Class<?> getServiceType() {
        return getServiceClass();
    }

    public T getRef() {
        return ref;
    }

    @Override
    public List<MethodSignature> getMethodSignature() {
        List<MethodSignature> services = new ArrayList<>();
        Method[] declaredMethods = serviceClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (Modifier.isPublic(declaredMethod.getModifiers())) {
                services.add(new SimpleMethodSignature(declaredMethod));
            }
        }
        return services;
    }

    private String[] buildArgsType(Parameter[] parameters) {
        String[] types = new String[0];
        if (parameters.length > 0) {
            types = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                types[i] = parameters[i].getType().getName();
            }
        }
        return types;
    }
}
