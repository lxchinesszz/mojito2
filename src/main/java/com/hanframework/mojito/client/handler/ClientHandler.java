package com.hanframework.mojito.client.handler;

import com.hanframework.mojito.future.MojitoFuture;

/**
 * @author liuxin
 * 2020-08-01 19:51
 */
public interface ClientHandler<T, R> {

    void received(R rpcResponse);

    MojitoFuture<R> async(T rpcRequest);

}
