package com.hanframework.mojito.processor;

/**
 * @author liuxin
 * 2020-09-01 17:17
 */
public interface Processor<T, R> {
    /**
     * 请求发送前处理器
     *
     * @param requestProcessors 请求处理器
     */
    void setRequestProcessor(RequestProcessor<T>[] requestProcessors);

    /**
     * 响应处理器
     *
     * @param responseProcessors 响应处理器
     */
    void setResponseProcessor(ResponseProcessor<R>[] responseProcessors);
}
