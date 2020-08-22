package com.hanframework.mojito.channel;

import java.net.InetSocketAddress;

/**
 * @author liuxin
 * @version Id: Endpoint.java, v 0.1 2019-05-11 10:54
 */
public interface Endpoint {

  InetSocketAddress getLocalAddress();

}
