package com.hanframework.mojito.handler;

import com.hanframework.kit.date.DatePatternEnum;
import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.handler.task.HandlerTask;
import com.hanframework.mojito.handler.task.HttpHandlerTask;
import com.hanframework.mojito.handler.task.RpcClientHandlerTask;
import com.hanframework.mojito.handler.task.RpcServerHandlerTask;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.handler.ServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author liuxin
 * 2020-07-25 22:18
 */
public class SingletonExchangeChannelHandler implements ExchangeChannelHandler {

    /**
     * 一旦确定了模型就不能再替换
     */
    private final Protocol protocol;

    /**
     * 用于记录连接数量
     */
    private final AtomicLong connections = new AtomicLong(0);

    /**
     * 是否服务端
     */
    private boolean isServer = true;

    @Override
    public ExchangeChannelHandler serverChannelHandler() {
        this.isServer = true;
        return this;
    }

    @Override
    public ExchangeChannelHandler clientChannelHandler() {
        this.isServer = false;
        return this;
    }


    public SingletonExchangeChannelHandler(Protocol protocol, boolean isServer) {
        this.protocol = protocol;
        this.isServer = isServer;
    }

    public SingletonExchangeChannelHandler(Protocol protocol) {
        this.protocol = protocol;
    }


    private static String toAddressString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

    /**
     * 接受连接
     *
     * @param channel channel.
     * @throws RemotingException 远程调用异常
     */
    public void connected(EnhanceChannel channel) throws RemotingException {
        connections.incrementAndGet();
        System.out.println(toAddressString(channel.getRemoteAddress()) + "已连接到服务端");
    }

    /**
     * 断开连接
     *
     * @param channel channel.
     * @throws RemotingException 远程调用异常
     */
    public void disconnected(EnhanceChannel channel) throws RemotingException {
        connections.decrementAndGet();
        System.out.println(toAddressString(channel.getRemoteAddress()) + "-->于" + DatePatternEnum.ZN_DATE_TIME_MS_PATTERN.format() + ",已断开到服务端");
    }

    /**
     * 数据写
     *
     * @param channel channel.
     * @param message message.
     * @throws RemotingException 远程执行异常
     */
    public void write(EnhanceChannel channel, Object message) throws RemotingException {
//        RpcResponse rpcResponse = new RpcResponse();
//        System.out.println("向" + toAddressString(channel.getRemoteAddress()) + "发送了一条数据:" + rpcResponse);
//        channel.sendAndClose(rpcResponse);
    }

    /**
     * 接受消息
     *
     * @param channel channel.
     * @param message message.
     * @throws RemotingException 远程调用异常
     */
    @SuppressWarnings("unchecked")
    public void read(EnhanceChannel channel, final Object message) throws RemotingException {
        Executor executor = protocol.getExecutor();
        ServerHandler serverHandler = protocol.getServerHandler();
        HandlerTask handlerTask;
        try {
            //1. 服务端的处理逻辑
            if (isServer) {
                if (message instanceof FullHttpRequest) {
                    HttpRequestFacade httpRequestFacade = new HttpRequestFacade((FullHttpRequest) message);
                    channel.setAttribute(KeepAlive.KEEPALIVE, httpRequestFacade.keepAlive());
                    handlerTask = new HttpHandlerTask(serverHandler, channel, httpRequestFacade);
                } else {
                    RpcProtocolHeader rpcProtocolHeader = (RpcProtocolHeader) message;
                    String keepAlive = rpcProtocolHeader.getAttachment(KeepAlive.KEEPALIVE);
                    channel.setAttribute(KeepAlive.KEEPALIVE, Boolean.valueOf(keepAlive));
                    handlerTask = new RpcServerHandlerTask(serverHandler, channel, (RpcProtocolHeader) message);
                }
            } else {
                //2. 客户端的处理逻辑
                handlerTask = new RpcClientHandlerTask(protocol, channel, message);
            }
            //3. 如果指定了线程池就交给线程池来处理
            if (executor != null) {
                executor.execute(handlerTask);
            } else {
                // 没有指定线程池就直接运行
                handlerTask.justStart();
            }
        } catch (Throwable throwable) {
            channel.exceptionCaught(new RemotingException(throwable));
            channel.disconnected();
        }
    }


    public void caught(EnhanceChannel channel, Throwable exception) throws RemotingException {
        channel.exceptionCaught(new RemotingException(exception));
    }
}
