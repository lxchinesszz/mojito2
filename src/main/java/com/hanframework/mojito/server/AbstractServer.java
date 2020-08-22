package com.hanframework.mojito.server;

import com.hanframework.kit.thread.ThreadHookTools;
import com.hanframework.mojito.banner.Banner;
import com.hanframework.mojito.exception.ProtocolException;
import com.hanframework.mojito.exception.SignatureException;
import com.hanframework.mojito.protocol.Protocol;
import com.hanframework.mojito.protocol.ProtocolManager;
import com.hanframework.mojito.signature.service.SignatureManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.logger.Logger;

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

    public void start(int port) {
        checked();
        if (isRunning()) {
            throw new RuntimeException("当前已经运行状态,不允许重复运行");
        }
        ThreadHookTools.addHook(new Thread(this::close));
        doOpen(port);
    }


    protected abstract void doOpen(int port);


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

    public void close() {
        log.info("Server 关闭成功");
        doClose();
    }

}
