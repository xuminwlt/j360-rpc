package me.j360.rpc.server;

import com.google.common.collect.Maps;
import me.j360.rpc.register.ServiceRegister;
import org.junit.Test;

import java.util.Map;

/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/24 下午10:44
 * 说明：
 */
public class ServerTest {


    @Test
    public void serverTest() {

        Map<String,Object> handlerMap = Maps.newHashMap();

        RPCServerOption rpcServerOption = new RPCServerOption();
        ServiceRegister serviceRegister = new ServiceRegister("localhost:2181");

        handlerMap.put(ServerTest.class.getCanonicalName(),new ServerTest());
        RPCServer rpcServer = new RPCServer(rpcServerOption,serviceRegister,handlerMap);
        rpcServer.start();


    }
}
