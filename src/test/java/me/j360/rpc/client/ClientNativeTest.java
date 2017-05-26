package me.j360.rpc.client;

import me.j360.rpc.model.UserDO;
import me.j360.rpc.register.ServiceDiscovery;
import me.j360.rpc.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/25 上午10:17
 * 说明：
 */
public class ClientNativeTest {


    RPCClientOption clientOption;
    RPCClient rpcClient;
    ServiceDiscovery serviceDiscovery;

    @Before
    public void before(){
        clientOption = new RPCClientOption();
        clientOption.setConnectTimeoutMillis(3000);
        clientOption.setKeepAlive(true);
        clientOption.setKeepAliveTime(3000);
        clientOption.setWriteTimeoutMillis(200);
        clientOption.setReadTimeoutMillis(500);

        rpcClient = new RPCClient(clientOption);

        rpcClient.init();

        serviceDiscovery = new ServiceDiscovery(rpcClient,"localhost:2181", UserService.class.getCanonicalName());
        serviceDiscovery.init();

    }

    @Test
    public void clientTest() {

        UserService userService = rpcClient.create(UserService.class);
        UserDO userDO = userService.getUser(1L);

        System.out.println(userDO.toString());
    }

    @Test
    public void clientAsyncTest() throws InterruptedException {
        RPCCallback<UserDO> rpcCallback = new RPCCallback<UserDO>() {
            @Override
            public void success(UserDO response) {
                System.out.println(response.toString());
            }

            @Override
            public void fail(Throwable e) {

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserService userService = rpcClient.createAsync(UserService.class, rpcCallback);
                userService.getUser(1L);

            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
    }

}
