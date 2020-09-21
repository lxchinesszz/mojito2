package com.hanframework.mojito.client;

/**
 * @author liuxin
 * 2020-09-18 22:20
 */
public interface Retry {

    /**
     * 是否重试
     *
     * @return boolean
     */
    boolean canRetry();

    /**
     * 是否重试,如果可以重试次数+1,true
     * 否则false
     *
     * @return boolean
     */
    boolean canRetryIncrease();

    /**
     * 重试次数
     *
     * @return int
     */
    int retryCount();

    /**
     * 当前重试次数
     *
     * @return int
     */
    int currentCount();

    /**
     * 重置重试
     */
    void reset();
}
