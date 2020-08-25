package com.hanframework.mojito.client.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.future.Promise;
import com.hanframework.mojito.protocol.ProtocolEnum;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author liuxin
 * 2020-08-23 00:14
 */
public abstract class AbstractAsyncClientPromiseHandler<T extends RpcProtocolHeader, R extends RpcProtocolHeader> implements ClientPromiseHandler<T, R> {

    /**
     * 当前通道正在发送的数据信息
     */
    private final Map<Long, Promise<R>> futureMap = new ConcurrentHashMap<>();

    /**
     * 通用的
     */
    private final List<Promise<R>> messageList = new CopyOnWriteArrayList<>();

    /**
     * 接受请求并从中拿到唯一id,执行返回值通知
     *
     * @param rpcResponse 请求
     */
    @Override
    public void received(R rpcResponse) {
        receivedBefore(rpcResponse);
        //如果是消息通知协议,就通知绑定的监听器
        if (ProtocolEnum.MQ_SEND == ProtocolEnum.byType(rpcResponse.getProtocolType())) {
            if (!messageList.isEmpty()) {
                for (Promise<R> rPromise : messageList) {
                    rPromise.setSuccess(rpcResponse);
                }
            }
        } else {
            // 从中拿到请求的future进行回告通知
            Promise<R> promise = futureMap.remove(rpcResponse.getId());
            promise.setSuccess(rpcResponse);
        }

    }

    @Override
    public MojitoFuture<R> async(EnhanceChannel enhanceChannel, T rpcRequest) {
        sendBefore(rpcRequest);
        enhanceChannel.send(rpcRequest);
        MojitoFuture<R> future = new MojitoFuture<>();
        if (ProtocolEnum.MQ_REG == ProtocolEnum.byType(rpcRequest.getProtocolType())) {
            messageList.add(future);
        } else {
            futureMap.put(rpcRequest.getId(), future);
        }
        return future;
    }


    /**
     * 发送前的处理
     *
     * @param rpcRequest 请求信息
     */
    public abstract void sendBefore(T rpcRequest);

    /**
     * 收到消息前
     *
     * @param rpcResponse 请求信息
     */
    public abstract void receivedBefore(R rpcResponse);
}
