package com.hanframework.mojito.test;

/**
 * @author liuxin
 * 2020-09-05 18:28
 */
public class MThreadGroup extends ThreadGroup {
    public MThreadGroup(String s) {
        super(s);
    }

    public MThreadGroup(ThreadGroup threadGroup, String s) {
        super(threadGroup, s);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        //任何一个线程异常就讲所有组内的线程中断
        thread.getThreadGroup().interrupt();
    }
}
