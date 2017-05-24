package me.j360.rpc.server;

import me.j360.rpc.register.ServiceDiscovery;
import me.j360.rpc.register.ServiceRegister;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/23 下午11:10
 * 说明：
 */

public class RegisterTest {

    @Test
    public void registerTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        ServiceRegister serviceRegister = new ServiceRegister("localhost:2181");
        serviceRegister.register("me.j360.rpc","127.0.0.1:20880");

        latch.await();
    }


    @Test
    public void registerDiscoverTest() throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("localhost:2181","me.j360.rpc");
        serviceDiscovery.init();

        //latch.await();
    }
}
