package me.j360.rpc.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;


/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/23 下午2:49
 * 说明：
 */

@Slf4j
public class RPCServiceTask implements Runnable{

    private Channel channel;
    private RpcRequest request;
    private Object object;

    private final Map<String, Object> handlerMap;

    public RPCServiceTask(Channel channel,RpcRequest rpcRequest, RPCServer rpcServer) {
        this.channel = channel;
        this.request = request;
        this.handlerMap = rpcServer.handlerMap;

    }

    @Override
    public void run() {

        RpcResponse response = new RpcResponse();
        try {
            response.setRequestId(request.getRequestId());
            Object result =  handle(request);
            response.setResult(result);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            response.setError("错误");
        }

        channel.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.debug("Send response for request " + request.getRequestId());
            }
        });

    }

    private Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        log.debug(serviceClass.getName());
        log.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            log.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < parameters.length; ++i) {
            log.debug(parameters[i].toString());
        }

        // JDK reflect
        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/

        // Cglib reflect
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

}
