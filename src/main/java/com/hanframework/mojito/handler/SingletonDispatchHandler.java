package com.hanframework.mojito.handler;

import com.hanframework.kit.date.DatePatternEnum;
import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.http.HttpRequestFacade;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.protocol.mojito.model.RpcResponse;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author liuxin
 * 2020-07-25 22:18
 */
public class SingletonDispatchHandler implements MojitoChannelHandler {

    private Protocol protocol;

    /**
     * 用于记录连接数量
     */
    private AtomicLong connections = new AtomicLong(0);

    /**
     * 是否服务端
     */
    private boolean isServer = true;

    @Override
    public MojitoChannelHandler serverChannelHandler() {
        this.isServer = true;
        return this;
    }

    @Override
    public MojitoChannelHandler clientChannelHandler() {
        this.isServer = false;
        return this;
    }


    public SingletonDispatchHandler(Protocol protocol, boolean isServer) {
        this.protocol = protocol;
        this.isServer = isServer;
    }

    public SingletonDispatchHandler(Protocol protocol) {
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
        Runnable runnable;
        try {
            if (isServer) {
                runnable = () -> {
                    System.out.println("ServerHandler处理" + toAddressString(channel.getRemoteAddress()) + "发送来的一条数据:" + message);
                    Object response;
                    boolean keepAlive = true;
                    if (message instanceof FullHttpRequest || message instanceof RpcProtocolHeader) {
                        try {
                            if (message instanceof FullHttpRequest) {
                                HttpRequestFacade httpRequestFacade = new HttpRequestFacade((FullHttpRequest) message);
                                keepAlive = httpRequestFacade.KeepAlive();
                                response = protocol.getServerHandler().handler(channel, httpRequestFacade);
                            } else {
                                response = protocol.getServerHandler().handler(channel, message);
                            }
                            // 服务端也可以对通道进行写,但是写完之后要将标记置位不可写。否则这里会在写一次
                            if (channel.isWrite()) {
                                if (keepAlive) {
                                    channel.send(response);
                                } else {
                                    channel.sendAndClose(response);
                                }
                            } else {
                                //重置可写
                                channel.markWrite();
                            }
                        } catch (Throwable throwable) {
                            System.err.println("Server1312321312");
                            channel.exceptionCaught(new RemotingException(throwable));
                            channel.disconnected();
                        }
                    }
                };
            } else {
                runnable = () -> {
                    System.out.println("ClientHandler收到来自服务端" + toAddressString(channel.getRemoteAddress()) + "返回的一条数据:" + message);
                    try {
                        protocol.getClientPromiseHandler().received(message);
                    } catch (Throwable throwable) {
                        System.err.println("Client1312321312");
                        channel.exceptionCaught(new RemotingException(throwable));
                        channel.disconnected();
                    }
                };
            }
            if (executor != null) {
                executor.execute(runnable);
            } else {
                runnable.run();
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
