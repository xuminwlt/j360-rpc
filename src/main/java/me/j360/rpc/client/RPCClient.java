package me.j360.rpc.client;

import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.core.Endpoint;

import java.net.InetSocketAddress;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/17 上午11:59
 * 说明：
 */

@Slf4j
public class RPCClient implements Endpoint{


    private final RPCClientOption rpcClientOption;
    private RPCConnectManager rpcConnectManager;

    public RPCClient(RPCClientOption rpcClientOption) {
        this.rpcClientOption = rpcClientOption;

        init();
    }


    public void init() {

        rpcConnectManager = RPCConnectManager.getInstance(rpcClientOption);
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


    public RPCClientOption getRpcClientOption() {
        return rpcClientOption;
    }

    public RPCConnectManager getRpcConnectManager() {
        return rpcConnectManager;
    }
}
