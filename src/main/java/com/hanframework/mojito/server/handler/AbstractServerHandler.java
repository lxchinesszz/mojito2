package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.protocol.mojito.model.RpcRequest;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import com.hanframework.mojito.protocol.mojito.model.RpcTools;
import com.hanframework.mojito.signature.service.SignatureHodler;
import com.hanframework.mojito.signature.service.SignatureManager;

import java.util.Objects;

/**
 * 判读处理的类是否有返回值,没有返回值的,处理完成直接就构建RpcResponse true。
 * 如果报错或者是false。
 * 如果有返回值但是不匹配，返回了null。也返回true.
 *
 * @author liuxin
 * 2020-08-01 19:06
 */
public abstract class AbstractServerHandler<T, R> implements ServerHandler<T, R> {


    @Override
    public R handler(T rpcRequest) throws RemotingException {
        //1. 过滤器?
        //2. 定义一些RpcResponse的子类，客户端判断是否拒绝
        R response;
        try {
            response = doHandler(rpcRequest);
        } catch (Exception e) {
            throw new RemotingException(e);
        }
        return response;
    }

    public abstract R doHandler(T rpcRequest) throws Exception;

}
