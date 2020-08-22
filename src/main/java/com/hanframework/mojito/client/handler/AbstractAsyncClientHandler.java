package com.hanframework.mojito.client.handler;

import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.future.Promise;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author liuxin
 * 2020-08-23 00:14
 */
public abstract class AbstractAsyncClientHandler<T extends RpcProtocolHeader, R extends RpcProtocolHeader> implements ClientHandler<T, R> {

    /**
     * 当前通道正在发送的数据信息
     */
    private final Map<Long, Promise<R>> futureMap = new ConcurrentHashMap<>();

    @Override
    public void received(R rpcResponse) {
        Promise<R> promise = futureMap.remove(rpcResponse.getId());
        promise.setData(rpcResponse);
        System.out.println("收到来自服务端响应:" + rpcResponse);
    }

    @Override
    public MojitoFuture<R> async(T rpcRequest) {
        MojitoFuture<R> future = new MojitoFuture<>();
        futureMap.put(rpcRequest.getId(), future);
        return future;
    }


}
