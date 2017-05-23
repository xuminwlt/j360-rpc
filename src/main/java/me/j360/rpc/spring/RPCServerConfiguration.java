package me.j360.rpc.spring;

import me.j360.rpc.register.ServiceRegister;
import me.j360.rpc.server.RPCServer;
import me.j360.rpc.server.RPCServerOption;
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
public class RPCServerConfiguration {

    private @Value("#{timeout}") Long timeout;
    private @Value("#{zkAddress}") String zkAdress;

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
    public RPCServer rpcServer() throws Exception {
        return new RPCServerFactoryBean(rpcServerOption(),serviceRegister()).getObject();
    }


}
