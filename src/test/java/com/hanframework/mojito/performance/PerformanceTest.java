package com.hanframework.mojito.performance;

import com.hanframework.kit.util.StopWatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * @author liuxin
 * 9/30/20 8:20 PM
 */
public class PerformanceTest {

    static int count = 1000000;

    static volatile ObjectContainer<Object> container = new ObjectContainer<>(count, Object::new);


    /**
     * 模拟创建10000个对象的耗时
     */
    @Test
    public synchronized void newObjectTest() {
        StopWatch sw = new StopWatch();
        count = 10000000;
        sw.start("new创建");
        //时间换空间
        newObject(count);
        sw.stop();
        sw.start("new2创建");
        //空间换时间
        newObject2(count);
        sw.stop();
        sw.start("new3创建");
        //空间换时间
        newObject3(count, container);
        sw.stop();
        System.out.println(sw.prettyPrint());

    }

    private void newObject(int count) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new Object());
        }
        System.out.println("List-size:" + list.size());
    }


    private void newObject2(int count) {
        ObjectContainer<Object> container = new ObjectContainer<>(count, Object::new);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(container.getObject());
        }
        System.out.println("List-size:" + list.size());
    }


    private void newObject3(int count, ObjectContainer<Object> container) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(container.getObject());
        }
        System.out.println("List-size:" + list.size());
    }

    static class ObjectContainer<T> {

        List<T> objList;

        int count;

        Supplier<T> createSupplier;

        public ObjectContainer(int count, Supplier<T> createSupplier) {
            this.count = count;
            this.createSupplier = createSupplier;
            this.initQueue();
        }


        private void initQueue() {
            objList = new LinkedList<T>();
            for (int i = 0; i < count; i++) {
                objList.add(createSupplier.get());
            }
        }

        private void resize() {
            int size = objList.size();
            if (size <= count * 0.75) {
                for (int i = 0; i < count - size; i++) {
                    objList.add(createSupplier.get());
                }
            }
        }

        public T getObject() {
            resize();
            return objList.remove(0);
        }
    }
}
