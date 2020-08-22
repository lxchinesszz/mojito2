package com.hanframework.mojito.server;

import com.hanframework.mojito.protocol.Protocol;

/**
 * @author liuxin
 * 2020-07-31 17:40
 */
public interface ProtocolRegister {

    void registerProtocol(Protocol protocol);

    Protocol getProtocol();
}
