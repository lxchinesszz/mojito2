package com.hanframework.mojito.queue;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * Disruptor: 粉碎机
 *
 * @author liuxin
 * 2020-09-01 16:20
 */
public class DisruptorTest {


    public static void main(String[] args) throws Exception {
        /**
         * 队列中放的数据
         */
        class Element {

            private int value;

            public Element(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        // 生产者的线程工厂
        ThreadFactory threadFactory = r -> new Thread(r, "simpleThread");


        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element(1);
            }
        };

        // 处理Event的handler
        EventHandler<Element> handler = new EventHandler<Element>() {
            @Override
            public void onEvent(Element element, long sequence, boolean endOfBatch) {
                System.out.println("Element: " + element.getValue());
            }
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定RingBuffer的大小
        int bufferSize = 16;

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);


        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor的线程
        disruptor.start();


        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        for (int l = 0; true; l++) {
            // 获取下一个可用位置的下标
//            long sequence = ringBuffer.next();
//            try {
//                // 返回可用位置的元素
//                Element event = ringBuffer.get(sequence);
//                // 设置该位置元素的值
//                event.setValue(l);
//            } finally {
//                ringBuffer.publish(sequence);
//            }

            int finalL = l;
            ringBuffer.publishEvent(new EventTranslator<Element>() {
                @Override
                public void translateTo(Element event, long sequence) {
                    event.setValue(1);
                }
            });
            Thread.sleep(10);
        }


    }
}
