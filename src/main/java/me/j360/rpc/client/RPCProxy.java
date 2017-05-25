package me.j360.rpc.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


@SuppressWarnings("unchecked")
@Slf4j
public class RPCProxy<T> implements MethodInterceptor {


    private RPCClient rpcClient;

    private Class<T> clazz;

    private Boolean async;

    private RPCCallback rpcCallback;

    public RPCProxy(RPCClient rpcClient, Class clazz, boolean async, RPCCallback rpcCallback) {
        this.clazz = clazz;
        this.rpcClient = rpcClient;
        this.async = async;
        this.rpcCallback = rpcCallback;
    }


    public static <T> T getProxy(RPCClient rpcClient, Class clazz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        en.setCallback(new RPCProxy(rpcClient,clazz,false,null));
        return (T) en.create();
    }

    public static <T> T getProxy(RPCClient rpcClient, Class clazz, boolean async, RPCCallback rpcCallback) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        en.setCallback(new RPCProxy(rpcClient,clazz,async,rpcCallback));
        return (T) en.create();
    }


    /**
     * 异步情况下返回null
     * @param obj
     * @param method
     * @param args
     * @param proxy
     * @return
     * @throws Throwable
     */
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {

        final String methodName = method.getName();
        final String interfaceName = method.getDeclaringClass().getCanonicalName();
        RpcRequest fullRequest = new RpcRequest();

        //logId, serviceName, methodName, args[0], method.getReturnType());
        RpcResponse response = new RpcResponse();
        fullRequest.setClassName(interfaceName);
        fullRequest.setMethodName(methodName);
        fullRequest.setParameters(args);
        fullRequest.setParameterTypes(method.getParameterTypes());

        //后期使用过类似Servlet滤器链解决多元配置化问题
        /*FilterChain filterChain = new ClientFilterChain(rpcClient.getFilters(), rpcClient);
        filterChain.doFilter(fullRequest, fullResponse);*/

        Channel channel = rpcClient.getRpcConnectManager().selectChannel(interfaceName);

        //在此处校验并使用同步或异步的判断+超时+其他的校验,分别调用DefaultFuture的不同的方法
        if (!async.booleanValue()) {
            DefaultFuture future = new DefaultFuture(fullRequest,rpcCallback,3000L);
            future.sent(channel);

            response = DefaultFuture.getFuture(fullRequest.getRequestId()).get();

            DefaultFuture.removeRPCFuture(fullRequest.getRequestId());

            return response.getResult();
        } else {
            //DefaultFuture future = new DefaultFuture(rpcClientHandler,fullRequest,rpcCallback);
            //DefaultFuture.sent(rpcClientHandler.getChannel(),fullRequest);

            return response;
        }
    }
}
