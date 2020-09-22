package com.hanframework.mojito.util;

import cn.hutool.core.collection.CollectionUtil;
import com.hanframework.mojito.protocol.http.HttpRequestParser;

import java.util.*;

/**
 * @author liuxin
 * 2020-09-22 21:41
 */
public class EnhanceServiceLoader<T> {

    private ServiceLoader<T> load;

    private EnhanceServiceLoader(Class<T> type) {
        load = ServiceLoader.load(type);
    }

    public static <T> EnhanceServiceLoader<T> ofType(Class<T> type) {
        return new EnhanceServiceLoader<>(type);
    }

    public T getAvailable() {
        Iterator<T> iterator = load.iterator();
        List<T> availableList = new ArrayList<>();
        while (iterator.hasNext()) {
            availableList.add(iterator.next());
        }
        System.out.println(availableList);
        return CollectionUtil.isNotEmpty(availableList) ? availableList.get(0) : null;
    }


    public static void main(String[] args) {
        EnhanceServiceLoader<HttpRequestParser> serviceLoader = EnhanceServiceLoader.ofType(HttpRequestParser.class);
        HttpRequestParser available = serviceLoader.getAvailable();
        System.out.println(available);
    }
}
