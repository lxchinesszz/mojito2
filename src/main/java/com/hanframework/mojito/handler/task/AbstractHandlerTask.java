package com.hanframework.mojito.handler.task;


import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.handler.KeepAlive;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.server.handler.ServerHandler;

/**
 * @author liuxin
 * 2020-09-14 17:43
 */
public abstract class AbstractHandlerTask<R, V> implements HandlerTask {

    private Protocol<R, V> protocol;

    private ServerHandler<R, V> serverHandler;

    private EnhanceChannel enhanceChannel;

    private R request;

    public AbstractHandlerTask(Protocol<R, V> protocol, EnhanceChannel enhanceChannel, R request) {
        this.protocol = protocol;
        this.enhanceChannel = enhanceChannel;
        this.request = request;
    }

    public AbstractHandlerTask(ServerHandler<R, V> serverHandler, EnhanceChannel enhanceChannel, R request) {
        this.serverHandler = serverHandler;
        this.enhanceChannel = enhanceChannel;
        this.request = request;
    }

    public Protocol<R, V> getProtocol() {
        return protocol;
    }

    public ServerHandler<R, V> getServerHandler() {
        return serverHandler;
    }

    public EnhanceChannel getEnhanceChannel() {
        return enhanceChannel;
    }

    public R getRequest() {
        return request;
    }

    public boolean keepAlive() {
        return Boolean.parseBoolean(String.valueOf(getEnhanceChannel().getAttribute(KeepAlive.KEEPALIVE)));
    }

    @Override
    public void justStart() {
        run();
    }

    @Override
    public void run() {
        try {
            V v = doResult();
            if (this instanceof RpcClientHandlerTask) {
                return;
            }
            // 服务端也可以对通道进行写,但是写完之后要将标记置位不可写。否则这里会在写一次
            EnhanceChannel enhanceChannel = getEnhanceChannel();
            if (enhanceChannel.isWrite()) {
                if (keepAlive()) {
                    enhanceChannel.send(v);
                } else {
                    enhanceChannel.sendAndClose(v);
                }
            } else {
                //重置可写
                enhanceChannel.markWrite();
            }
        } catch (Throwable throwable) {
            enhanceChannel.exceptionCaught(new RemotingException(throwable));
            enhanceChannel.disconnected();
        }
    }

    public abstract V doResult();
}
