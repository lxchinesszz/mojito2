package com.hanframework.mojito.protocol.echo;

import com.hanframework.mojito.channel.EnhanceChannel;
import com.hanframework.mojito.client.handler.ClientPromiseHandler;
import com.hanframework.mojito.exception.RemotingException;
import com.hanframework.mojito.future.MojitoFuture;
import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.handler.MojitoCoreHandler;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.mojito.model.RpcProtocolHeader;
import com.hanframework.mojito.server.handler.ServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executor;

/**
 * 客户端发送什么数据,返回什么数据
 *
 * @author liuxin
 * 2020-08-02 02:10
 */
public class EchoProtocol implements Protocol {
    @Override
    public String name() {
        return "echo";
    }

    @Override
    public MojitoChannelHandler getRequestHandler() {
        return new MojitoCoreHandler(this);
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public ChannelDecoder getRequestDecoder() {
        return null;
    }

    @Override
    public ChannelEncoder getResponseEncoder() {
        return new ChannelEncoder("echo") {

            @Override
            public void doEncode(ChannelHandlerContext ctx, RpcProtocolHeader msg, ByteBuf out) throws Exception {
//                ByteBuf byteBuf = (ByteBuf) msg;
//                ((ByteBuf) msg).resetReaderIndex();
//                byte[] dataArr = new byte[byteBuf.readableBytes()];
//                byteBuf.readBytes(dataArr);
//                out.writeBytes(dataArr);
            }
        };
    }

    @Override
    public ServerHandler<ByteBuf, ByteBuf> getServerHandler() {
        return new ServerHandler<ByteBuf, ByteBuf>() {

            @Override
            public ByteBuf handler(EnhanceChannel channel,ByteBuf rpcRequest) throws RemotingException {
                System.out.println("执行一段逻辑处理");
                return rpcRequest;
            }
        };
    }

    @Override
    public ClientPromiseHandler getClientPromiseHandler() {
        return new ClientPromiseHandler() {

            @Override
            public MojitoFuture async(EnhanceChannel enhanceChannel, Object rpcRequest) {
                return null;
            }

            @Override
            public void received(Object rpcResponse) {

            }
        };
    }
}
