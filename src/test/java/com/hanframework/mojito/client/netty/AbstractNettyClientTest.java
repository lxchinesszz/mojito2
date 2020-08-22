package com.hanframework.mojito.client.netty;

import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.protocol.mojito.MojitoProtocol;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.serialization.SerializeEnum;
import com.hanframework.mojito.test.pojo.User;
import com.hanframework.mojito.test.service.UserService;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author liuxin
 * 2020-08-01 17:38
 */
public class AbstractNettyClientTest {

    @Test
    public void testNettyClient() throws Exception {
        AbstractNettyClient abstractNettyClient = new DefaultNettyClient(new MojitoProtocol());
        abstractNettyClient.connect("127.0.0.1", 8888);

        StopWatch stopWatch = new StopWatch();
        for (int i = 1; i <= 1; i++) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setProtocolType(ProtocolEnum.MOJITO.getType());
            rpcRequest.setSerializationType(SerializeEnum.HESSION2.getType());
            rpcRequest.setReturnType(User.class);
            rpcRequest.setServiceType(UserService.class);
            stopWatch.start("第" + i + "次");
            MojitoFuture<RpcResponse> future = abstractNettyClient.sendAsync(rpcRequest);
            RpcResponse response = future.get();
            System.out.println(response);
            System.out.println(future.get());
            stopWatch.stop();
        }
        System.out.println(stopWatch.prettyPrint());
        abstractNettyClient.close();
    }

    @Test
    public void testNettyClientTimeout() throws Exception {
        DefaultNettyClient abstractNettyClient = new DefaultNettyClient(new MojitoProtocol());
        abstractNettyClient.connect("127.0.0.1", 8888);
        StopWatch stopWatch = new StopWatch();
        for (int i = 1; i <= 1; i++) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setProtocolType(ProtocolEnum.MOJITO.getType());
            rpcRequest.setSerializationType(SerializeEnum.HESSION2.getType());
            rpcRequest.setReturnType(User.class);
            rpcRequest.setServiceType(UserService.class);
            stopWatch.start("第" + i + "次");
            MojitoFuture<RpcResponse> future = abstractNettyClient.sendAsync(rpcRequest);
            System.out.println(future.get(10, TimeUnit.MILLISECONDS));
            stopWatch.stop();
        }
        System.out.println(stopWatch.prettyPrint());
        abstractNettyClient.close();
    }

    @Test
    public void futureNullTest() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> submit = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Object o = submit.get();
        System.out.println(o);
    }

    @Test
    public void futureNotNullTest() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> submit = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 100;
            }
        });

        Integer o = submit.get();
        System.out.println(o);
//        FutureTask
        FutureTask s;
//        ThreadPoolExecutor
    }

}