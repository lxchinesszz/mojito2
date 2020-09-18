package com.hanframework.mojito.monitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控接口
 * 1. 服务期间的总请求量
 * 2. 服务期间请求平均耗时
 * 3. 线程池状态
 *
 * @author liuxin
 * 2020-09-17 14:07
 */
public interface Monitor {

    AtomicInteger d = new AtomicInteger();

    /**
     * 增加总请求量
     */
    void increment();
}
