package com.hanframework.mojito.future;


/**
 * 承诺-任务在执行过程中有哪些状态?
 * 1. 完成
 * 2. 失败
 * 3. 执行中
 * 任务状态从未决 - 到完成。
 * 如何知道完成呢?
 * 1. setSuccess 标识成功
 * 2. setFailure 标识失败
 *
 * @author liuxin
 * 2020-08-16 16:01
 */
public interface Promise<V> {

    /**
     * 是否成功
     *
     * @return boolean
     */
    boolean isSuccess();

    /**
     * 设置成功表示
     *
     * @param data 数据
     */
    void setSuccess(V data);

    /**
     * 设置失败标识
     *
     * @param cause 异常
     * @return this
     */
    void setFailure(Throwable cause);

}
