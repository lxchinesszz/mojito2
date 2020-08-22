package com.hanframework.mojito.signature.method;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author liuxin
 * 2020-07-31 23:05
 */
public class SimpleMethodSignature implements MethodSignature {

    private Method method;

    private Class<?> classType;

    private Class<?>[] parameterTypes;

    private Class<?> returnType;

    private Integer modifiers;

    public SimpleMethodSignature(Method method) {
        this.method = method;
        this.classType = method.getDeclaringClass();
        this.parameterTypes = method.getParameterTypes();
        this.returnType = method.getReturnType();
        this.modifiers = method.getModifiers();
    }

    @Override
    public Class<?> classType() {
        if (Objects.isNull(classType)) {
            classType = method.getDeclaringClass();
        }
        return classType;
    }

    @Override
    public Class<?>[] paramters() {
        if (Objects.isNull(parameterTypes)) {
            parameterTypes = method.getParameterTypes();
        }
        return parameterTypes;
    }

    @Override
    public Class<?> returnType() {
        if (Objects.isNull(returnType)) {
            returnType = method.getReturnType();
        }
        return returnType;
    }

    @Override
    public int getModifier() {
        if (Objects.isNull(modifiers)) {
            modifiers = method.getModifiers();
        }
        return modifiers;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public String toString() {
        return "SimpleMethodSignature{" +
                "method=" + method +
                '}';
    }
}
