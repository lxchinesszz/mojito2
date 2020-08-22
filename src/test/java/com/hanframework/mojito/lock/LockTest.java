package com.hanframework.mojito.lock;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxin
 * 2020-08-13 19:33
 */
public class LockTest {

    @Test
    public void lockTest() throws Exception {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println("A开始执行");
                }
//                lock.unlock();
            }
        }, "A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
//                condition.signal();
                for (int i = 0; i < 5; i++) {
                    System.out.println("B开始执行");
                }
                lock.unlock();
            }
        }, "B").start();


    }


}
