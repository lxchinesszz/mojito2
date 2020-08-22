package com.hanframework.mojito.future;


/**
 * @author liuxin
 * 2020-08-16 16:01
 */
public interface Promise<V> {

    boolean isSuccess();

    void setData(V data);

}
