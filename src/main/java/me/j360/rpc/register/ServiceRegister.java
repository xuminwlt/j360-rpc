package me.j360.rpc.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;

/**
 * Package: me.j360.rpc.register
 * User: min_xu
 * Date: 2017/5/23 下午1:22
 * 说明：
 */
public class ServiceRegister {


    private CountDownLatch latch = new CountDownLatch(1);

    private String zkAddress;

    private static CuratorFramework client;

    public ServiceRegister(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    private void init() {

    }


    public void register(String data) {
        if (data != null) {
            client = getClient();
            if (client != null) {

            }
        }
    }


    private void createNode(String data) {

    }

    private CuratorFramework getClient() {
        try {
            client =  CuratorFrameworkFactory.builder().connectString(zkAddress)
                    .retryPolicy(new RetryNTimes(RETRY_TIME, RETRY_INTERVAL))
                    .connectionTimeoutMs(CONNECT_TIMEOUT).build();

            return client;
            latch.countDown();
        } catch (Exception e) {

        }
        return null;
    }
}
