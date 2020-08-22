package com.hanframework.mojito.handler;

/**
 * @author liuxin
 * 2020-07-23 23:08
 */
public interface ChannelRequestHandler<T> {

    void handlerRequest(T t);
}
