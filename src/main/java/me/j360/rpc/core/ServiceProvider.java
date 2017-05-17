package me.j360.rpc.core;

import lombok.Data;

import java.util.Map;

/**
 * Package: me.j360.rpc.core
 * User: min_xu
 * Date: 2017/5/17 下午1:03
 * 说明：
 */

@Data
public class ServiceProvider {

    //完整的服务名称
    private String serviceName;

    private Map<IpPort,ProviderConnection> connectionMap;

}
