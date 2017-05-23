package me.j360.rpc.client;

import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.core.Consumer;
import me.j360.rpc.core.Endpoint;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/17 上午11:59
 * 说明：
 */

@Slf4j
public class RPCClient implements Endpoint{


    public RPCClientOption rpcClientOption;


    public RPCClient(List<Consumer> consumers,RPCClientOption rpcClientOption) {

        if (rpcClientOption != null) {
            this.rpcClientOption = rpcClientOption;
        } else {
            this.rpcClientOption = new RPCClientOption();
        }
    }


    public <T> T create(Class<T> interfaceClass) {
        /*return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass)
        ); */
        return RPCProxy.getProxy(this,interfaceClass);
    }

    public <T> T createAsync(Class<T> interfaceClass,RPCCallback callback) {
        /*return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass)
        ); */
        return RPCProxy.getProxy(this,interfaceClass,true,callback);
    }


    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }




}
