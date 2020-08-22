package com.hanframework.mojito.signature.method;


import java.lang.reflect.Method;

/**
 * @author liuxin
 * 2020-07-25 21:29
 */
public interface MethodSignature {

    /**
     * 方法宿主
     *
     * @return Class
     */
    Class<?> classType();

    /**
     * 方法参数
     *
     * @return Class
     */
    Class<?>[] paramters();

    /**
     * 方法返回值
     *
     * @return Class
     */
    Class<?> returnType();

    /**
     * 方法修饰符
     * @return int
     */
    int getModifier();

    /**
     * 获取方法
     * @return Method
     */
    Method getMethod();


}
