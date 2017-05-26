package me.j360.rpc.spring;

import me.j360.rpc.register.ServiceRegister;
import me.j360.rpc.server.RPCServer;
import me.j360.rpc.server.RPCServerOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: me.j360.rpc.spring
 * User: min_xu
 * Date: 2017/5/23 下午5:39
 * 说明：
 */

@Configuration
public class RPCServerConfiguration {

    //private @Value("#{timeout}") Long timeout = 3000L;
    //private @Value("#{zkAddress}") String zkAdress = "127.0.0.1:2181";

    private String zkAdress = "127.0.0.1:2181";

    @Bean
    public RPCServerOption rpcServerOption() {
        RPCServerOption rpcServerOption = new RPCServerOption();
        return new RPCServerOption();
    }

    @Bean
    public ServiceRegister serviceRegister() {
        return new ServiceRegister(zkAdress);
    }


    @Bean
    public RPCServerFactoryBean rpcServer() {
        RPCServerFactoryBean rpcServerFactoryBean = new RPCServerFactoryBean();
        rpcServerFactoryBean.setRpcServerOption(rpcServerOption());
        rpcServerFactoryBean.setServiceRegister(serviceRegister());
        return rpcServerFactoryBean;
    }

    @Bean
    public RPCServer rpcServer2() throws Exception {
        return rpcServer().getObject();
    }



}
