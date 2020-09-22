package com.hanframework.mojito.util;

import io.netty.buffer.ByteBuf;

/**
 * @author liuxin
 * 2020-09-22 21:18
 */
public final class ByteBufUtils {

    public static String toString(ByteBuf buf) {
        String str;
        if (buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }


}
