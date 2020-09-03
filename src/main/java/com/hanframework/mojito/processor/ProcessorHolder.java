package com.hanframework.mojito.processor;

/**
 * @author liuxin
 * 2020-09-01 22:39
 */
public final class ProcessorHolder<T, R> {

    private final RequestProcessor<T>[] requestProcessors;

    private final ResponseProcessor<R>[] responseProcessors;

    public ProcessorHolder(RequestProcessor<T>[] requestProcessors, ResponseProcessor<R>[] responseProcessors) {
        this.requestProcessors = requestProcessors;
        this.responseProcessors = responseProcessors;
    }


    public void sendBeforeProcess(final T rpcRequest) {
        if (this.requestProcessors != null && this.requestProcessors.length > 0) {
            for (RequestProcessor<T> requestProcessor : this.requestProcessors) {
                requestProcessor.requestPreProcessor(rpcRequest);
            }
        }
    }

    /**
     * 发送前的处理,比如设置执行的协议信息
     *
     * @param rpcRequest 请求信息
     */
    public void sendAfterProcess(final T rpcRequest) {
        if (this.requestProcessors != null && this.requestProcessors.length > 0) {
            for (RequestProcessor<T> requestProcessor : this.requestProcessors) {
                requestProcessor.requestPostProcessor(rpcRequest);
            }
        }
    }

    public void receivedBeforeProcess(final R rpcResponse) {
        if (this.responseProcessors != null && this.responseProcessors.length > 0) {
            for (ResponseProcessor<R> responseProcessor : this.responseProcessors) {
                responseProcessor.responsePreProcessor(rpcResponse);
            }
        }
    }

    /**
     * 收到消息前
     *
     * @param rpcResponse 请求信息
     */
    public void receivedAfterProcess(final R rpcResponse) {
        if (this.responseProcessors != null && this.responseProcessors.length > 0) {
            for (ResponseProcessor<R> responseProcessor : this.responseProcessors) {
                responseProcessor.responsePostProcessor(rpcResponse);
            }
        }
    }

}
