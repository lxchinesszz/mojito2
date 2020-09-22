package com.hanframework.mojito.protocol.http;

import com.hanframework.mojito.config.Constant;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.util.FullHttpRequestUtils;
import io.netty.handler.codec.http.HttpMessage;

import java.util.Map;

/**
 * @author liuxin
 * 2020-09-16 21:41
 */
public class BaseHttpMessage extends RpcProtocolHeader implements HttpProtocolHeader {

    /**
     * 协议版本
     */
    protected final String protocolVersion;

    /**
     * 请求头或者响应头
     */
    protected final Map<String, String> headers;

    /**
     * 协议头
     */
    protected final HttpMessage httpMessage;

    /**
     * 原始id
     */
    protected String originId;


    public BaseHttpMessage(HttpMessage httpMessage, boolean server) {
        this.httpMessage = httpMessage;
        this.protocolVersion = httpMessage.protocolVersion().protocolName();
        this.headers = FullHttpRequestUtils.parseHeaders(httpMessage);
        //服务端才从请求头中获取id
        if (server) {
            originId = getHeaders().get(Constant.REQUEST_ID);
        } else {
            originId = getId();
        }
    }


    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void addHeader(String header, String value) {
        httpMessage.headers().add(header, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getOriginId() {
        return originId;
    }

    @Override
    public void setOriginId(String originId) {
        getHeaders().put(Constant.REQUEST_ID, originId);
        httpMessage.headers().set(Constant.REQUEST_ID, originId);
        setId(originId);
        this.originId = originId;
    }

    @Override
    public String toString() {
        return "BaseHttpMessage{" +
                "protocolVersion='" + protocolVersion + '\'' +
                ", headers=" + headers +
                '}';
    }
}
