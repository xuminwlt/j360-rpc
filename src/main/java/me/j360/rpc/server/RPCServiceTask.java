package me.j360.rpc.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.codec.protobuf.RPCMessage;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;


/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/23 下午2:49
 * 说明：
 */

@Slf4j
public class RPCServiceTask implements Runnable{

    private Channel channel;
    private RPCMessage request;
    private Object object;

    public RPCServiceTask() {

    }

    @Override
    public void run() {

        byte[] responseBody = (byte[]) handle(request);
        channel.writeAndFlush(responseBody).addListener(new ChannelFutureListener() {
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

        LOGGER.debug(serviceClass.getName());
        LOGGER.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            LOGGER.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < parameters.length; ++i) {
            LOGGER.debug(parameters[i].toString());
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
