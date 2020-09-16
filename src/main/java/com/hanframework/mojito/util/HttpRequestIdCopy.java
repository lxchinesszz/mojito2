package com.hanframework.mojito.util;

import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.http.HttpResponseFacade;

import java.util.Objects;

/**
 * http协议数据的传输原始报文还是FullHttpRequest & FullHttpResponse
 * 为了适应框架会将数据包装成HttpRequestFacade & HttpResponseFacade
 * 如何保证在长连接下内容传输是一一对应的,要根据请求id进行判断
 * RPC协议因为继承了RpcProtocolHeader,同时RpcProtocolHeader会在网络中传输,所以RpcProtocolHeader中的id就是请求id
 * 而HTTP协议虽然也适配成了RpcProtocolHeader,即HttpRequestFacade的父类,但是网络中传输的并不是RpcProtocolHeader,而是FullHttpRequest
 * 和FullHttpResponse,所以需要将请求唯一id作为http的请求头中传递
 * <p>
 * 服务端,从FullHttpRequest中奖request-id读取并放到FullHttpResponse的响应头中(当前工具类就是做这一步使用)
 *
 * @author liuxin
 * 2020-09-16 21:22
 */
public class HttpRequestIdCopy {

    public static void copyRequestId(HttpRequestFacade httpRequestFacade, HttpResponseFacade httpResponseFacade) {
        String originId = httpRequestFacade.getOriginId();
        //当时web请求时候可能就没有请求头,此时不需要请求头
        if (Objects.nonNull(originId)) {
            //拷贝头id
            httpResponseFacade.setId(originId);
            //拷贝请求id
            httpResponseFacade.setOriginId(originId);
        }
    }
}
