package com.hanframework.mojito.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuxin
 * 2020-09-18 22:34
 */
public class CountRetryImpl implements Retry {

    private final Integer record;

    private AtomicInteger count;

    public CountRetryImpl(int count) {
        this.count = new AtomicInteger(count);
        this.record = count;
    }

    @Override
    public boolean canRetry() {
        return count.get() > 0;
    }

    @Override
    public boolean canRetryIncrease() {
        if (canRetry()) {
            count.decrementAndGet();
            return true;
        }
        return false;
    }

    @Override
    public int currentCount() {
        return record - count.get();
    }

    @Override
    public int retryCount() {
        return record;
    }

    @Override
    public void reset() {
        this.count = new AtomicInteger(record);
    }

}
