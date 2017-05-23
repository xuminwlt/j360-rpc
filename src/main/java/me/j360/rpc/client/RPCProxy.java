package me.j360.rpc.client;

import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.client.handler.RPCClientHandler;
import me.j360.rpc.codec.RPCHeader;
import me.j360.rpc.codec.RPCMessage;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.UUID;


@SuppressWarnings("unchecked")
@Slf4j
public class RPCProxy<T> implements MethodInterceptor {


    private RPCClient rpcClient;

    private Class<T> clazz;

    public RPCProxy(RPCClient rpcClient, Class clazz) {
        this.clazz = clazz;
        this.rpcClient = rpcClient;
    }

    public static <T> T getProxy(RPCClient rpcClient, Class clazz) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        en.setCallback(new RPCProxy(rpcClient,clazz));
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

        RPCClientHandler rpcClientHandler = RPCConnectManager.getInstance(rpcClient.rpcClientOption).selectHandler();
        DefaultFuture future = new DefaultFuture(rpcClientHandler);
        DefaultFuture.sent(rpcClientHandler.getChannel(),fullRequest);

        //在此处校验并使用同步或异步的判断+超时+其他的校验,分别调用DefaultFuture的不同的方法
        fullResponse = DefaultFuture.getFuture(fullRequest.getHeader().getLogId()).get();

        return fullResponse.getBodyMessage();
    }

}
