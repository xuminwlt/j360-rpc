package me.j360.rpc.client;

import me.j360.rpc.model.UserDO;
import me.j360.rpc.register.ServiceDiscovery;
import me.j360.rpc.service.UserService;
import org.junit.Test;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/25 上午10:17
 * 说明：
 */
public class ClientTest {


    @Test
    public void clientTest() {
        RPCClientOption clientOption = new RPCClientOption();
        clientOption.setConnectTimeoutMillis(3000);
        clientOption.setKeepAlive(true);
        clientOption.setKeepAliveTime(3000);
        clientOption.setWriteTimeoutMillis(200);
        clientOption.setReadTimeoutMillis(500);

        RPCClient rpcClient = new RPCClient(clientOption);

        rpcClient.init();

        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(rpcClient,"localhost:2181", UserService.class.getCanonicalName());
        serviceDiscovery.init();


        UserService userService = rpcClient.create(UserService.class);
        UserDO userDO = userService.getUser(1L);

        System.out.println(userDO.toString());
    }
}
