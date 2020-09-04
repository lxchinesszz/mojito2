package com.hanframework.mojito.config;

/**
 * @author liuxin
 * 2020-09-04 19:03
 */
public class UserInvoker {
    public String getName(String name) {
        return "反射执行:" + name;
    }
}
