package com.hanframework.mojito.future.listener;

/**
 * @author liuxin
 * 2020-08-15 18:01
 */
public interface MojitoListener<T> {

    void success(T t);

    void fail(Throwable t);
}
