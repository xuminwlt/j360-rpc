package me.j360.rpc.client;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/23 下午3:58
 * 说明：
 */
public interface AsyncProxy<T> {

    <T> DefaultFuture call(String funcName, Object... args) ;
}
