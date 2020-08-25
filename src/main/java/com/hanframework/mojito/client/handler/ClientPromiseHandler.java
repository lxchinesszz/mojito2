package com.hanframework.mojito.client.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.future.MojitoFuture;

/**
 * @author liuxin
 * 2020-08-01 19:51
 */
public interface ClientPromiseHandler<T, R> {

    void received(R rpcResponse);

    MojitoFuture<R> async(EnhanceChannel enhanceChannel, T rpcRequest);

}
