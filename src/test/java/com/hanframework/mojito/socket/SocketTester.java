package com.hanframework.mojito.socket;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liuxin
 * 2020-08-15 01:17
 */
public class SocketTester {

    @Test
    public void socketTest()throws Exception{
        //1. 创建一个服务端的通道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.socket().bind(new InetSocketAddress("127.0.0.1",10086));


        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selector.select();
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()){
            SelectionKey currentAccept = iterator.next();
            iterator.remove();
        }
    }
}
