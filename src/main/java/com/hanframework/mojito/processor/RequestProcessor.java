package com.hanframework.mojito.processor;

/**
 * 这个能力真的是很鸡肋，提供了这样的接口? 自己却不用？
 *
 * @author liuxin
 * 2020-09-01 17:03
 */
public interface RequestProcessor<T> {

    /**
     * 请求前处理器
     *
     * @param request 请求信息
     */
    void requestPreProcessor(final T request);

    /**
     * 请求后处理器
     *
     * @param request 请求
     */
    void requestPostProcessor(final T request);
}
