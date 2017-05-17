package me.j360.rpc.client;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/17 下午4:35
 * 说明：
 */
public class DefaultFuture implements ResponseFuture {

    private static final Map<Long, Channel> CHANNELS   = new ConcurrentHashMap<Long, Channel>();

    //保存请求及返回的对象
    private static final Map<Long, DefaultFuture> FUTURES   = new ConcurrentHashMap<Long, DefaultFuture>();





}
