package com.hanframework.mojito.proxy;

/**
 * 代理类接口
 * 1. jdk代理
 * 2. cglib代理
 *
 * @author liuxin
 * 2020-07-25 21:09
 */
public interface Proxy {

    /**
     * 生成代理对象
     *
     * @return Object
     */
    Object proxy();
}
