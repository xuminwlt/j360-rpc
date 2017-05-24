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

    private static String ZK_REGISTRY_PATH = "/j360-rpc";
    private static String ZK_PROVIDER_PATH = "/provider";

    private String zkAddress;

    private static CuratorFramework client;

    public ServiceRegister(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    private void init() {

    }


    public void register(String interfaceName, String serviceAddress) {
        if (interfaceName != null) {
            client = getClient();
            if (client != null) {
                createNode(interfaceName, serviceAddress);
            }
        }
    }


    /**
     * 注册服务及服务地址
     * @param interfaceName
     * @param serviceAddress
     */
    private void createNode(String interfaceName, String serviceAddress) {
        try {
            Stat stat = client.checkExists().forPath(ZK_REGISTRY_PATH);
            if (null == stat) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(ZK_REGISTRY_PATH, new byte[0]);
            }
            stat = client.checkExists().forPath(ZK_REGISTRY_PATH + "/" + interfaceName);
            if (null == stat) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(ZK_REGISTRY_PATH + "/" + interfaceName, new byte[0]);
                client.create().withMode(CreateMode.PERSISTENT).forPath(ZK_REGISTRY_PATH + "/" + interfaceName + ZK_PROVIDER_PATH, new byte[0]);
            }
            String registerAddress = ZK_REGISTRY_PATH + "/" + interfaceName + ZK_PROVIDER_PATH + "/" + serviceAddress;
            client.create().withMode(CreateMode.EPHEMERAL).forPath(registerAddress,serviceAddress.getBytes());
        } catch (Exception e) {
            log.error("创建节点失败:[{},{}]",interfaceName, serviceAddress,e);
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
