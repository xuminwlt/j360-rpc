package me.j360.rpc.spring;

import me.j360.rpc.register.ServiceRegister;
import me.j360.rpc.server.RPCServer;
import me.j360.rpc.server.RPCServerOption;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: me.j360.rpc.spring
 * User: min_xu
 * Date: 2017/5/23 下午3:40
 * 说明：use with @import
 */

public class RPCServerFactoryBean implements FactoryBean<RPCServer>, InitializingBean, DisposableBean, ApplicationContextAware {

    private Map<String, Object> handlerMap = new HashMap<>();

    private RPCServer rpcServer;

    private RPCServerOption rpcServerOption;

    private ServiceRegister serviceRegister;


    public RPCServerFactoryBean(){

    }


    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getCanonicalName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        rpcServer = new RPCServer(rpcServerOption,serviceRegister,handlerMap);
        rpcServer.start();
    }

    @Override
    public RPCServer getObject() throws Exception {
        return rpcServer;
    }

    @Override
    public Class<?> getObjectType() {
        return RPCServer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        //销毁Server
        rpcServer.shutdown();
    }

    public void setRpcServerOption(RPCServerOption rpcServerOption) {
        this.rpcServerOption = rpcServerOption;
    }

    public void setServiceRegister(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }
}
