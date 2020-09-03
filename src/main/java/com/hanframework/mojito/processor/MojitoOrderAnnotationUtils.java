package com.hanframework.mojito.processor;

import java.util.*;

/**
 * 对标记有
 *
 * @author liuxin
 * 2020-09-01 23:01
 * @see MojitoOrder 进行排序
 */
public class MojitoOrderAnnotationUtils {


    public static <T> void sort(T[] orders) {
        List<T> resultList = new ArrayList<>(orders.length);
        Collections.addAll(resultList, orders);
        Collections.sort(resultList, new Comparator<T>() {
            @Override
            public int compare(T t, T t1) {
                MojitoOrder preOrder = t.getClass().getAnnotation(MojitoOrder.class);
                int pre = preOrder != null ? preOrder.value() : 0;
                MojitoOrder postOrder = t1.getClass().getAnnotation(MojitoOrder.class);
                int post = postOrder != null ? postOrder.value() : 0;
                return pre - post;
            }
        });
    }
}
