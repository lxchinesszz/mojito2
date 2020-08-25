package com.hanframework.mojito.client;

import com.hanframework.mojito.future.listener.MojitoListener;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxin
 * 2020-08-15 15:39
 */
public class MojitoFuture<T> {

    private Channel channel;

    private Object value;

    private List<MojitoListener> listeners = new ArrayList<>();

    public MojitoFuture(Channel channel) {
        this.channel = channel;
    }

    public boolean isDone() {
        return value != null;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object get() {
        //阻塞这里
        while (!isDone()) {

        }
        return value;
    }


    public void addListener(MojitoListener listener) {
        listeners.add(listener);
    }


}
