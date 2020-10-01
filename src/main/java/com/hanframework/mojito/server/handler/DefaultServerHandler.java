package com.hanframework.mojito.server.handler;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.exception.BusinessServerHandlerException;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;

import java.util.Objects;

/**
 * 这一层目的是能让框架做更多的扩展
 *
 * @author liuxin
 * 2020-08-01 19:06
 */
public class DefaultServerHandler<T extends RpcProtocolHeader, R extends RpcProtocolHeader> implements ServerHandler<T, R> {


    /**
     * 业务方要定义的处理器
     */
    private BusinessHandler<T, R> businessHandler;

    @Override
    public void initWrapper(BusinessHandler<T, R> businessHandler) {
        this.businessHandler = businessHandler;
    }

    @Override
    public boolean inited() {
        return businessHandler != null;
    }

    private void checked() {
        if (Objects.isNull(businessHandler)) {
            throw new BusinessServerHandlerException(BusinessHandler.class + "不能为空,请先指定业务处理器");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public R handler(EnhanceChannel channel, T rpcRequest) throws RemotingException {
        //1. 过滤器?
        //2. 定义一些RpcResponse的子类，客户端判断是否拒绝
        checked();
        final R response;
        try {
            response = businessHandler.handler(new DefaultChannelContext(channel), rpcRequest);
            response.setId(rpcRequest.getId());
            response.setProtocolType(rpcRequest.getProtocolType());
            response.setSerializationType(rpcRequest.getSerializationType());
        } catch (Exception e) {
            throw new RemotingException(e);
        }
        return response;
    }


}
