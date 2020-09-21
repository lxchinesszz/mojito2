package com.hanframework.mojito.queue;

import com.hanframework.kit.string.StringTools;
import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.client.Client;
import com.hanframework.mojito.config.Installer;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.future.listener.MojitoListener;
import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.handler.SubServerHandler;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author liuxin
 * 2020-08-24 23:08
 */
public class QueueTest implements Serializable {

    private class Message extends RpcProtocolHeader {

        /**
         * 路由键
         */
        private String routeKey;

        /**
         * 消息信息
         */
        private String message;

        public Message(String routeKey, String message) {
            this.routeKey = routeKey;
            this.message = message;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "routeKey='" + routeKey + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    private class QueueStatus extends RpcProtocolHeader {
        private String message;

        public QueueStatus(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "QueueStatus{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    @Test
    public void queueTest() {
        Installer.server(Message.class, QueueStatus.class)

                .serverHandler(new SubServerHandler<Message, QueueStatus>() {

                    //1. 收到消息之后如果处理成功就返回给客户端。

                    //2. 如果是订阅的链接,就启动服务去消费。
                    private Map<String, List<EnhanceChannel>> routeKeyChannelMap = new ConcurrentHashMap<>();

                    private Map<String, Queue<String>> routeKeyQueueMap = new ConcurrentHashMap<>();

                    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);


                    @Override
                    public QueueStatus handler(EnhanceChannel channel, Message message) {
                        ProtocolEnum protocolEnum = ProtocolEnum.byType(message.getProtocolType());
                        //1. 如果发现是注册协议,就将当前客户端的连接给保存到指定的topic里面
                        if (protocolEnum == ProtocolEnum.MQ_REG) {
                            List<EnhanceChannel> enhanceChannels = routeKeyChannelMap.computeIfAbsent(message.routeKey, k -> new ArrayList<>());
                            enhanceChannels.add(channel);
                            return new QueueStatus("订阅成功");
                        } else if (protocolEnum == ProtocolEnum.MQ_SEND) {
                            //2. 如果发现是发送协议,就向指定的topic去添加一条消息
                            Queue<String> queue = routeKeyQueueMap.get(message.routeKey);
                            if (queue == null) {
                                queue = new ArrayBlockingQueue<String>(23);
                                routeKeyQueueMap.put(message.routeKey, queue);
                            }
                            queue.add(message.message);
                            //3. 启动一个线程去将topic信息,推送给客户端
                            messageProcessing();
                            return new QueueStatus("发送成功");
                        }

                        //推送给客户端,并收到客户端消息。看是否要把消息移出。
                        return new QueueStatus("未知");
                    }


                    private void messageProcessing() {
                        if (!atomicBoolean.get()) {
                            atomicBoolean.compareAndSet(atomicBoolean.get(), true);
                            new Thread(() -> {
                                while (true) {
                                    for (Map.Entry<String, Queue<String>> queueEntry : routeKeyQueueMap.entrySet()) {
                                        String routeKey = queueEntry.getKey();
                                        Queue<String> routeKeyMessages = queueEntry.getValue();
                                        String message = routeKeyMessages.poll();
                                        if (!StringTools.isBlank(message)) {
                                            //获取到订阅的链接
                                            List<EnhanceChannel> enhanceChannels = routeKeyChannelMap.get(routeKey);
                                            for (EnhanceChannel enhanceChannel : enhanceChannels) {
                                                if (enhanceChannel.isConnected()) {
                                                    Message serverMsg = new Message(routeKey, message);
                                                    serverMsg.setProtocolType(ProtocolEnum.MQ_SEND.getType());
                                                    enhanceChannel.send(serverMsg);
                                                } else {
                                                    enhanceChannels.remove(enhanceChannel);
                                                }
                                            }
                                        }
                                    }
                                }
                            }).start();
                        }
                    }
                }).create().start(12306);

    }


    @Test
    public void subscriberTest1() throws Exception {
        Client<Message, QueueStatus> client = Installer.client(Message.class, QueueStatus.class).create();
        client.connect("127.0.0.1", 12306);
        Message message = new Message("testRouteKey", "第一条链接");
        message.setProtocolType(ProtocolEnum.MQ_REG.getType());
        MojitoFuture<QueueStatus> queueStatusMojitoFuture = client.sendAsync(message);
        queueStatusMojitoFuture.addListener(new MojitoListener<QueueStatus>() {
            @Override
            public void onSuccess(QueueStatus result) throws Exception {
                System.out.println("收到消息:" + result);
            }

            @Override
            public void onThrowable(Throwable throwable) throws Exception {

            }
        });
        QueueStatus queueStatus = queueStatusMojitoFuture.get();
        System.out.println(queueStatus);
        while (true) ;
    }

    @Test
    public void subscriberTest2() throws Exception {
        Client<Message, QueueStatus> client = Installer.client(Message.class, QueueStatus.class).create();
        client.connect("127.0.0.1", 12306);
        Message message = new Message("testRouteKey", "第3条链接");
        message.setProtocolType(ProtocolEnum.MQ_SEND.getType());
        MojitoFuture<QueueStatus> queueStatusMojitoFuture = client.sendAsync(message);
        QueueStatus queueStatus = queueStatusMojitoFuture.get();
        System.out.println(queueStatus);
        while (true) ;
    }

}
