package me.j360.rpc.client;

import me.j360.rpc.codec.RPCHeader;
import me.j360.rpc.codec.RPCMessage;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;


@SuppressWarnings("unchecked")
public class RPCProxy implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RPCProxy.class);

    private RPCClient rpcClient;

    public RPCProxy(RPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public static <T> T getProxy(RPCClient rpcClient, Class clazz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        en.setCallback(new RPCProxy(rpcClient));
        return (T) en.create();
    }

    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {

        final String logId = UUID.randomUUID().toString();
        final String serviceName = method.getDeclaringClass().getSimpleName();
        final String methodName = method.getName();

        RPCMessage<RPCHeader.RequestHeader> fullRequest = DefaultFuture.buildFullRequest(
                logId, serviceName, methodName, args[0], method.getReturnType());

        RPCMessage<RPCHeader.ResponseHeader> fullResponse = new RPCMessage<>();


        /*FilterChain filterChain = new ClientFilterChain(rpcClient.getFilters(), rpcClient);
        filterChain.doFilter(fullRequest, fullResponse);*/

        return fullResponse.getBodyMessage();
    }

}
