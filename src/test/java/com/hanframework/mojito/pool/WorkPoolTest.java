package com.hanframework.mojito.pool;

import com.hanframework.kit.util.StopWatch;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.mojito.MojitoChannelDecoder;
import org.junit.Test;


/**
 * @author liuxin
 * 2020-09-04 14:54
 */
public class WorkPoolTest {

    /**
     * StopWatch '测试池化和非池化的性能差别': running time (millis) = 402
     * -----------------------------------------
     * ms     %     Task name
     * -----------------------------------------
     * 00402  100%
     * <p>
     * 总堆大小:247M
     * 空闲堆大小:202M
     * 已用堆大小:44M
     *
     * @throws Exception 异常
     */
    @Test
    public void testWorkPool() throws Exception {
        WorkPool<ChannelDecoder> workPool = new WorkPool<>(() -> new MojitoChannelDecoder(""));

        StopWatch stopWatch = new StopWatch("测试池化和非池化的性能差别");
        stopWatch.start();
        for (int i = 0; i < 1000000; i++) {
            ChannelDecoder object = workPool.getObject();
            workPool.returnObject(object);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        monitoring();
    }

    /**
     * StopWatch '测试池化和非池化的性能差别': running time (millis) = 97
     * -----------------------------------------
     * ms     %     Task name
     * -----------------------------------------
     * 00097  100%
     * <p>
     * 总堆大小:247M
     * 空闲堆大小:221M
     * 已用堆大小:26M
     *
     * @throws Exception 异常
     */
    @Test
    public void testNotWorkPool() throws Exception {
        StopWatch stopWatch = new StopWatch("测试池化和非池化的性能差别");
        stopWatch.start();
        for (int i = 0; i < 1000000; i++) {
            new MojitoChannelDecoder("");
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        monitoring();
    }


    public void monitoring() {
        //获取当前堆的大小 byte 单位
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("总堆大小:" + heapSize / 1014 / 1024 + "M");
        //获取当前空闲的内存容量byte单位
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("空闲堆大小:" + heapFreeSize / 1014 / 1024 + "M");

        System.out.println("已用堆大小:" + (heapSize - heapFreeSize) / 1024 / 1024 + "M");
    }

}