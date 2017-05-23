package me.j360.rpc.register;

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

    private volatile List<String> dataList = new ArrayList<>();

    public ServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
    }


    public String discover() {
        return null;
    }

}
