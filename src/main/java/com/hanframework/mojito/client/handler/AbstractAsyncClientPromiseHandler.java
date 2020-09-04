package com.hanframework.mojito.client.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.future.Promise;
import com.hanframework.mojito.processor.Processor;
import com.hanframework.mojito.processor.ProcessorHolder;
import com.hanframework.mojito.processor.RequestProcessor;
import com.hanframework.mojito.processor.ResponseProcessor;
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
public class AbstractAsyncClientPromiseHandler<T extends RpcProtocolHeader, R extends RpcProtocolHeader> implements ClientPromiseHandler<T, R>, Processor<T, R> {

    /**
     * 当前通道正在发送的数据信息
     */
    private final Map<Long, Promise<R>> futureMap = new ConcurrentHashMap<>();

    /**
     * 通用的
     */
    private final List<Promise<R>> messageList = new CopyOnWriteArrayList<>();

    /**
     * 请求处理器
     */
    private RequestProcessor<T>[] requestProcessors;

    /**
     * 响应处理器
     */
    private ResponseProcessor<R>[] responseProcessors;

    /**
     * 对请求和响应处理器的封装
     */
    private ProcessorHolder<T, R> processorHolder;

    /**
     * 接受请求并从中拿到唯一id,执行返回值通知
     *
     * @param rpcResponse 请求
     */
    @Override
    public void received(R rpcResponse) {
        receivedBeforeProcess(rpcResponse);
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
        receivedAfterProcess(rpcResponse);
    }

    @Override
    public MojitoFuture<R> sendAsync(EnhanceChannel enhanceChannel, T rpcRequest) {
        MojitoFuture<R> future;
        try {
            sendBeforeProcess(rpcRequest);
            enhanceChannel.send(rpcRequest);
            future = new MojitoFuture<>();
            if (ProtocolEnum.MQ_REG == ProtocolEnum.byType(rpcRequest.getProtocolType())) {
                messageList.add(future);
                futureMap.put(rpcRequest.getId(), future);
            } else {
                futureMap.put(rpcRequest.getId(), future);
            }
        } finally {
            sendAfterProcess(rpcRequest);
        }
        return future;
    }


    /**
     * 发送前的处理,比如设置执行的协议信息
     *
     * @param rpcRequest 请求信息
     */
    private void sendBeforeProcess(T rpcRequest) {
        if (processorHolder != null) {
            processorHolder.sendBeforeProcess(rpcRequest);
        }
    }

    /**
     * 发送前的处理,比如设置执行的协议信息
     *
     * @param rpcRequest 请求信息
     */
    private void sendAfterProcess(T rpcRequest) {
        if (processorHolder != null) {
            processorHolder.sendAfterProcess(rpcRequest);
        }
    }

    private void receivedBeforeProcess(R rpcResponse) {
        if (processorHolder != null) {
            processorHolder.receivedBeforeProcess(rpcResponse);
        }
    }

    /**
     * 收到消息前
     *
     * @param rpcResponse 请求信息
     */
    public void receivedAfterProcess(R rpcResponse) {
        if (processorHolder != null) {
            processorHolder.receivedAfterProcess(rpcResponse);
        }
    }

    @Override
    public void setRequestProcessor(RequestProcessor<T>[] requestProcessors) {
        this.requestProcessors = requestProcessors;
    }

    @Override
    public void setResponseProcessor(ResponseProcessor<R>[] responseProcessors) {
        this.responseProcessors = responseProcessors;
    }
}
