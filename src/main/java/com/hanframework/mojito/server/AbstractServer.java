package com.hanframework.mojito.server;

import com.hanframework.kit.thread.ThreadHookTools;
import com.hanframework.mojito.exception.ProtocolException;
import com.hanframework.mojito.protocol.Protocol;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 支持基于HTTP/HTTPS/及RPC
 * <p>
 * 发送一些事件信息
 *
 * @author liuxin
 * 2020-07-25 21:15
 */
@Slf4j
public abstract class AbstractServer implements Server {


    private AtomicBoolean running = new AtomicBoolean(false);

    private boolean async;

    private int port;

    @Override
    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void start(int port) {
        start0(port, false);
    }

    @Override
    public void startAsync(int port) {
        start0(port, true);
    }

    private void start0(int port, boolean async) {
        checked();
        if (isRunning()) {
            throw new RuntimeException("当前已经运行状态,不允许重复运行");
        }
        ThreadHookTools.addHook(new Thread(this::close));
        doOpen(port, async);
    }

    protected abstract void doOpen(int port, boolean async);


    protected abstract void doClose();


    private void checked() {
        Protocol protocol = getProtocol();
        if (protocol == null) {
            throw new ProtocolException("请指定协议处理器");
        }
    }

    private boolean isRunning() {
        if (!running.get()) {
            running.set(true);
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        log.info("Server 关闭成功");
        doClose();
    }

}
