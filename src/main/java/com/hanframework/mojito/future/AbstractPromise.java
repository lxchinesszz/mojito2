package com.hanframework.mojito.future;

import com.hanframework.mojito.future.listener.MojitoListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxin
 * 2020-08-16 16:02
 */
public abstract class AbstractPromise<V> implements Promise<V>, Future<V> {

    /**
     * 数据
     */
    private V data;

    /**
     * 是否撤销
     */
    private boolean cancelled = false;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private Condition timeoutCondition = lock.newCondition();

    private List<MojitoListener<V>> listeners;

    public AbstractPromise() {
    }

    @Override
    public boolean isSuccess() {
        return isDone();
    }

    @Override
    public void setData(V data) {
        lock.lock();
        this.data = data;
        condition.signal();
        timeoutCondition.signal();
        lock.unlock();
        notityAll();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!isCancelled() && !isSuccess()) {
            this.cancelled = mayInterruptIfRunning;
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
        return this.data != null;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        try {
            lock.lock();
            if (!isDone() && !isCancelled()) {
                //拦截
                condition.await();
            }
        } finally {
            lock.unlock();
        }
        return data;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        lock.lock();
        boolean await = timeoutCondition.await(timeout, unit);
        if (!await && !isDone()) {
            throw new TimeoutException();
        }
        lock.unlock();
        return data;
    }


    public List<MojitoListener<V>> getListeners() {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        return this.listeners;
    }

    public void addListener(MojitoListener listener) {
        getListeners().add(listener);
    }

    public void removeListener(MojitoListener listener) {
        getListeners().remove(listener);
    }

    public void notityAll() {
        List<MojitoListener<V>> listeners = getListeners();
        for (MojitoListener<V> listener : listeners) {
            listener.success(this.data);
        }
    }
}
