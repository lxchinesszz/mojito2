package com.hanframework.mojito.processor;

/**
 * @author liuxin
 * 2020-09-01 17:05
 */
public interface ResponseProcessor<R> {
    /**
     * 响应前处理器
     *
     * @param response 响应信息
     */
    void responsePreProcessor(R response);

    /**
     * 响应后处理器
     *
     * @param response 响应请求
     */
    void responsePostProcessor(R response);
}
