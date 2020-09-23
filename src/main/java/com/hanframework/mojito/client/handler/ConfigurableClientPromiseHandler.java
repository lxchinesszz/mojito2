package com.hanframework.mojito.client.handler;

import com.hanframework.mojito.processor.Processor;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;

/**
 * @author liuxin
 * 2020-09-01 17:15
 */
public interface ConfigurableClientPromiseHandler<T extends RpcProtocolHeader, R extends RpcProtocolHeader> extends Processor<T, R> {

}
