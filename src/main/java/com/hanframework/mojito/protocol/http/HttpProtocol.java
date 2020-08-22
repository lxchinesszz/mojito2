package com.hanframework.mojito.protocol.http;

import com.hanframework.mojito.client.handler.ClientHandler;
import com.hanframework.mojito.handler.MojitoChannelHandler;
import com.hanframework.mojito.protocol.ChannelDecoder;
import com.hanframework.mojito.protocol.ChannelEncoder;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.server.handler.ServerHandler;

import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2020-07-31 18:06
 */
public class HttpProtocol implements Protocol {

    @Override
    public String name() {
        return "http";
    }

    @Override
    public MojitoChannelHandler getRequestHandler() {
        return null;
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public ChannelDecoder getRequestDecoder() {
//        return new ChannelDecoder() {
////            HttpServerCodec httpServerCodec = new HttpServerCodec.HttpServerRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize,
////                    validateHeaders, initialBufferSize)
////            @Override
////            public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
////                httpServerCodec.
////            }
//        };
        return null;
    }

    @Override
    public ChannelEncoder getResponseEncoder() {
        return null;
    }

    @Override
    public ServerHandler getServerHandler() {
        return null;
    }

    @Override
    public ClientHandler getClientHandler() {
        return null;
    }
}
