package com.hanframework.mojito.protocol;

import com.hanframework.mojito.protocol.mojito.MojitoProtocol;


/**
 * 自定义协议
 *
 * @author liuxin
 * 2020-07-31 19:27
 */
public class CustomerProtocolManager implements ProtocolManager {

    @Override
    public Protocol getDefaultProtocol() {
        return new MojitoProtocol();
    }

}
