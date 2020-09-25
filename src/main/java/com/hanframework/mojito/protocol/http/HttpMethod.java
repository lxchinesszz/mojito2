package com.hanframework.mojito.protocol.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxin
 * 2020-09-04 21:36
 */
public enum HttpMethod {
    GET("get"), POST("post"), PUT("put");

    private String name;

    private static Map<String, HttpMethod> cache = new ConcurrentHashMap<>();

    HttpMethod(String name) {
        this.name = name;
    }

    static {
        init();
    }

    public static void init() {
        for (HttpMethod value : values()) {
            cache.put(value.name, value);
        }
    }

    public static HttpMethod ofByName(String name) {
        if (cache.isEmpty()) {
            init();
        }
        return cache.get(name.toLowerCase());
    }
}
