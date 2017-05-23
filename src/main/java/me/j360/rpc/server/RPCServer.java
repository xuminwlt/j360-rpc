package me.j360.rpc.server;

import me.j360.rpc.core.Endpoint;
import me.j360.rpc.register.ServiceRegister;

import java.net.InetSocketAddress;

/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/17 下午12:20
 * 说明：
 */
public class RPCServer implements Endpoint {


    private RPCServerOption rpcServerOption;
    private ServiceRegister serviceRegister;

    public RPCServer(RPCServerOption rpcServerOption, ServiceRegister serviceRegister) {
        this.rpcServerOption = rpcServerOption;
        this.serviceRegister = serviceRegister;
    }


    /**
     * 启动NettyServer
     * 执行注册
     * 启动Server执行器
     *
     */
    public void start() {

    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }


    public
}
