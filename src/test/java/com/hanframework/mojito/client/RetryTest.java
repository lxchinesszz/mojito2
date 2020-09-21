package com.hanframework.mojito.client;


import org.junit.Test;

/**
 * @author liuxin
 * 2020-09-18 22:39
 */
public class RetryTest {

    @Test
    public void retryTest() {
        CountRetryImpl countRetry = new CountRetryImpl(3);
        whileTest(countRetry);
        if (!countRetry.canRetry()) {
            countRetry.reset();
        }
        whileTest(countRetry);
    }

    @Test
    public void notRetryTest() {
        Retry retry = new CountRetryImpl(0);
        whileTest(retry);
    }

    public void whileTest(Retry countRetry) {
        System.out.println("-------------start-----------------");
        System.out.println("重试次数:" + countRetry.retryCount());
        System.out.println("是否可以重试:" + countRetry.canRetry());
        while (countRetry.canRetryIncrease()) {
            System.out.println("当前重试次数:" + countRetry.currentCount());
        }

        System.out.println("--------------end----------------");
    }
}