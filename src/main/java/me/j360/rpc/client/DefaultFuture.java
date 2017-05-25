package me.j360.rpc.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/17 下午4:35
 * 说明：
 */

@Slf4j
public class DefaultFuture<T> implements ResponseFuture {

    private static final Map<Long, Channel> CHANNELS   = new ConcurrentHashMap<Long, Channel>();

    //保存请求及返回的对象
    private static final Map<Long, DefaultFuture> FUTURES   = new ConcurrentHashMap<Long, DefaultFuture>();

    private static AtomicLong atomicLong = new AtomicLong();

    private static ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private CountDownLatch latch;
    private ScheduledFuture scheduledFuture;
    private RpcRequest fullRequest;
    private RPCCallback<T> callback;

    private RpcResponse fullResponse;
    private Throwable error;
    private Long readTimeout;

    public DefaultFuture(RpcRequest fullRequest,
                     RPCCallback<T> callback,Long readTimeout) {
        /*if (fullRequest.getResponseBodyClass() == null && callback == null) {
            log.error("responseClass or callback must have one not null only");
            return;
        }*/
        this.fullRequest = fullRequest;
        this.scheduledFuture = scheduledFuture;
        this.callback = callback;
        /*if (this.fullRequest.getResponseBodyClass() == null) {
            Type type = callback.getClass().getGenericInterfaces()[0];
            Class clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            this.fullRequest.setResponseBodyClass(clazz);
        }*/

        scheduledExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                DefaultFuture rpcFuture = DefaultFuture.removeRPCFuture(fullRequest.getRequestId());
                if (rpcFuture != null) {
                    //log.debug("request timeout, logId={}, service={}, method={}",logId, serviceName, methodName);
                    rpcFuture.timeout();
                } else {
                    //log.debug("request logId={} not found", logId);
                }
            }
        }, readTimeout, TimeUnit.MILLISECONDS);

        this.latch = new CountDownLatch(1);

    }

    public void success(RpcResponse response) {
        this.fullResponse = response;
        scheduledFuture.cancel(true);

        latch.countDown();
        if (callback != null) {
            callback.success((T) fullResponse);
        }
    }

    public void fail(Throwable error) {
        this.error = error;
        scheduledFuture.cancel(true);

        latch.countDown();
        if (callback != null) {
            callback.fail(error);
        }
    }

    public void timeout() {
        this.fullResponse = null;
        latch.countDown();
        if (callback != null) {
            callback.fail(new RuntimeException("timeout"));
        }
    }

    public RpcResponse get() throws InterruptedException {
        latch.await();
        if (error != null) {
            log.warn("error occurs due to {}", error.getMessage());;
        }
        if (fullResponse == null) {
            //return timeout new RpcResponse;
        }
        return fullResponse;
    }

    public RpcResponse get(long timeout, TimeUnit unit) {
        try {
            if (latch.await(timeout, unit)) {
                if (error != null) {
                    log.warn("error occurrs due to {}", error.getMessage());

                }
            } else {
                log.warn("sync call time out");

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("sync call is interrupted, {}", e);

        }
        if (fullResponse == null) {

        }
        return fullResponse;
    }

    public static DefaultFuture getFuture(Long id) {
        DefaultFuture future = FUTURES.get(id);
        return future;
    }

    public static boolean hasFuture(Channel channel) {
        return CHANNELS.containsValue(channel);
    }



    public void sent(Channel channel) {
        fullRequest.setRequestId(atomicLong.incrementAndGet());

        FUTURES.put(fullRequest.getRequestId(),DefaultFuture.this);
        CHANNELS.put(fullRequest.getRequestId(),channel);

        channel.writeAndFlush(fullRequest);
    }


    public static DefaultFuture removeRPCFuture(Long id) {
        DefaultFuture future = FUTURES.get(id);
        if (future != null) {
            FUTURES.remove(id);
            CHANNELS.remove(id);
        }
        return future;
    }



}
