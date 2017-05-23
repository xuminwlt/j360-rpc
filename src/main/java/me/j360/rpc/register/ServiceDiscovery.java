package me.j360.rpc.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: me.j360.rpc.register
 * User: min_xu
 * Date: 2017/5/23 下午1:22
 * 说明：
 */
public class ServiceDiscovery {

    private String zkAddress;


    private static int RETRY_TIME  = 3;
    private static int RETRY_INTERVAL = 3000;
    private static int CONNECT_TIMEOUT = 3000;

    private static String ZK_REGISTRY_PATH = "/registry";
    private static String ZK_DATA_PATH = "/data";


    private volatile List<String> dataList = new ArrayList<>();

    public ServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    private static CuratorFramework client;

    private void init() {

        client = getClient();
        if (client != null) {
            watchNode();
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


    public static List<String> watchedGetChildren(String path, Watcher watcher) throws Exception {
        /**
         * Get children and set the given watcher on the node.
         */
        return client.getChildren().usingWatcher(watcher).forPath(path);
    }

    private void watchNode() {

        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    watchNode();
                }
            }
        };

        try {
            dataList = watchedGetChildren(ZK_REGISTRY_PATH + ZK_DATA_PATH,watcher);
        } catch (Exception e) {

        }


    }

}
