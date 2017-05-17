package me.j360.rpc.core;

import java.net.InetSocketAddress;

/**
 * Package: me.j360.rpc.core
 * User: min_xu
 * Date: 2017/5/17 上午11:55
 * 说明：
 */
public interface Endpoint {


    InetSocketAddress getLocalAddress();

}
