package me.j360.rpc.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Package: me.j360.rpc.register
 * User: min_xu
 * Date: 2017/5/23 下午1:22
 * 说明：
 */

@Slf4j
public class ServiceRegister {


    private CountDownLatch latch = new CountDownLatch(1);

    private static int RETRY_TIME  = 3;
    private static int RETRY_INTERVAL = 3000;
    private static int CONNECT_TIMEOUT = 3000;

    private static String ZK_REGISTRY_PATH = "/registry";
    private static String ZK_DATA_PATH = "/data";

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
                createNode(data);
            }
        }
    }


    private void createNode(String data) {
        try {
            Stat stat = client.checkExists().forPath(ZK_REGISTRY_PATH);
            if (null == stat) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(ZK_DATA_PATH, new byte[0]);
            }

            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(ZK_DATA_PATH,data.getBytes());
        } catch (Exception e) {
            log.error("创建节点失败:[{}]",data,e);
        }
    }

    private CuratorFramework getClient() {
        try {
            client =  CuratorFrameworkFactory.builder().connectString(zkAddress)
                    .retryPolicy(new RetryNTimes(RETRY_TIME, RETRY_INTERVAL))
                    .connectionTimeoutMs(CONNECT_TIMEOUT).build();
            client.start();
            return client;
        } catch (Exception e) {

        }
        return null;
    }
}
