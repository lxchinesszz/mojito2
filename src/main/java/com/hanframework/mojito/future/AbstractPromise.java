package com.hanframework.mojito.future;

import com.hanframework.mojito.future.listener.MojitoListeners;
import com.hanframework.mojito.future.listener.MojitoListener;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxin
 * 2020-08-16 16:02
 */
public abstract class AbstractPromise<V> implements Promise<V>, Future<V> {

    /**
     * volatile只能保证多线程可见性,不能保证原子性
     */
    private volatile V result;

    /**
     * 原子更新volatile result
     */
    private static final AtomicReferenceFieldUpdater<AbstractPromise, Object> RESULT_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(AbstractPromise.class, Object.class, "result");

    /**
     * 是否已通知监听器
     */
    private boolean notifyingListenersFlag;

    /**
     * 因为有多种监听器所以用Object,通过判断类型,找到对应的方法执行。
     * 因为监听器属于实例属性,每个任务都有自己的监听器。
     *
     * @see MojitoListener
     * @see MojitoListeners
     */
    private Object listeners;

    /**
     * 等待的任务数量
     */
    private short waiters;

    /**
     * 是否撤销
     */
    private boolean cancelled = false;

    /**
     * 当操作被撤销时,使用当前默认值
     */
    private static final Object UNCANCELLABLE = new Object();

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private Condition timeoutCondition = lock.newCondition();


    public AbstractPromise() {
    }

    @Override
    public boolean isSuccess() {
        return isDone() && this.result != UNCANCELLABLE;
    }

    @Override
    public void setSuccess(V result) {
        lock.lock();
        setValue0(result);
        condition.signal();
        timeoutCondition.signal();
        lock.unlock();
        notifyListeners();
    }

    @Override
    public void setFailure(Throwable cause) {

    }

    /**
     * 如果结果被重新赋值,则唤醒当前等待的任务
     *
     * @param objResult 结果
     * @return 设置结果
     */
    private boolean setValue0(Object objResult) {
        if (RESULT_UPDATER.compareAndSet(this, null, objResult) ||
                RESULT_UPDATER.compareAndSet(this, UNCANCELLABLE, objResult)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!isCancelled() && !isSuccess()) {
            this.cancelled = mayInterruptIfRunning;
            setValue0(UNCANCELLABLE);
            return true;
        }
        return false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean isDone() {
        return this.result != null;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        try {
            lock.lock();
            ++waiters;
            if (!isDone() && !isCancelled()) {
                //拦截
                condition.await();
            }
        } finally {
            lock.unlock();
            --waiters;
        }
        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            lock.lock();
            ++waiters;
            boolean await = timeoutCondition.await(timeout, unit);
            if (!await && !isDone()) {
                throw new TimeoutException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            --waiters;
            lock.unlock();
        }
        return result;
    }


    public void addListener(MojitoListener<V> listener) {
        synchronized (this) {
            this.listeners = listener;
        }
        if (isDone()) {
            notifyListeners();
        }
    }

    public void addListeners(MojitoListeners<V> listeners) {
        synchronized (this) {
            this.listeners = listeners;
        }
        if (isDone()) {
            notifyListeners();
        }
    }

    /**
     * 通知触发监听器
     */
    public void notifyListeners() {
        notifyListenersNow();
    }

    /**
     * 1. 根据监听器类型分别触发不同类型的监听器
     * 2. 已经通知过的或者没有配置监听器的不用通知
     */
    @SuppressWarnings("all")
    private void notifyListenersNow() {
        Object listeners;
        synchronized (this) {
            // 已经通知或者没有监听器就直接返回
            if (notifyingListenersFlag || this.listeners == null) {
                return;
            }
            listeners = this.listeners;
        }
        while (!notifyingListenersFlag) {
            if (listeners instanceof MojitoListeners) {
                MojitoListener<V>[] listeners0 = ((MojitoListeners<V>) listeners).listeners();
                for (MojitoListener<V> mojitoListener : listeners0) {
                    notifyListenersNow0(mojitoListener);
                }
            } else {
                notifyListenersNow0((MojitoListener<V>) listeners);
            }
            // 释放监听器
            this.listeners = null;
            // 否则更新为通知
            this.notifyingListenersFlag = true;
        }
    }

    private void notifyListenersNow0(MojitoListener<V> listener) {
        try {
            if (isSuccess()) {
                listener.onSuccess(result);
            } else {
                listener.onThrowable(null);
            }
        } catch (Exception e) {

        }

    }


}
