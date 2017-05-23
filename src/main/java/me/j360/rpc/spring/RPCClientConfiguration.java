package me.j360.rpc.spring;

import me.j360.rpc.client.RPCClient;
import me.j360.rpc.client.RPCClientOption;
import me.j360.rpc.register.ServiceDiscovery;
import me.j360.rpc.server.RPCServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Package: me.j360.rpc.spring
 * User: min_xu
 * Date: 2017/5/23 下午5:39
 * 说明：
 */

@Import(RPCServer.class)
@Configuration
public class RPCClientConfiguration {

    private @Value("#{timeout}") Long timeout;
    private @Value("#{zkAddress}") String zkAddress;

    @Bean
    public RPCClientOption rpcClientOption() {


        return new RPCClientOption();
    }

    @Bean
    public ServiceDiscovery serviceDiscovery() {
        return new ServiceDiscovery(zkAddress);
    }

    @Bean
    public RPCClient rpcClient() {
        return null;
    }

}
