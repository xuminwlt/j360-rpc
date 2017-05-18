package me.j360.rpc.client;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/17 下午10:43
 * 说明：调用类,接替Client的调用执行功能,封装Request,Future,请求,返回,完成Filter的过滤
 *
 * 执行Client对于连接的维护
 *
 */
public class ClientInvoke {

    //充当callback作用,将callback作为参数传进DefaultFuture进行执行

    public ClientInvoke(RPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    private RPCClient rpcClient;

    /*public DefaultFuture<T> invoke() {

        try {
            boolean isAsync = RpcUtils.isAsync(getUrl(), invocation);
            boolean isOneway = RpcUtils.isOneway(getUrl(), invocation);
            int timeout = getUrl().getMethodParameter(methodName, Constants.TIMEOUT_KEY,Constants.DEFAULT_TIMEOUT);
            if (isOneway) {
                boolean isSent = getUrl().getMethodParameter(methodName, Constants.SENT_KEY, false);
                currentClient.send(inv, isSent);
                RpcContext.getContext().setFuture(null);
                return new RpcResult();
            } else if (isAsync) {
                ResponseFuture future = currentClient.request(inv, timeout) ;
                RpcContext.getContext().setFuture(new FutureAdapter<Object>(future));
                return new RpcResult();
            } else {
                RpcContext.getContext().setFuture(null);
                return (Result) currentClient.request(inv, timeout).get();
            }
        } catch (TimeoutException e) {
            throw new RpcException(RpcException.TIMEOUT_EXCEPTION, "Invoke remote method timeout. method: " + invocation.getMethodName() + ", provider: " + getUrl() + ", cause: " + e.getMessage(), e);
        } catch (RemotingException e) {
            throw new RpcException(RpcException.NETWORK_EXCEPTION, "Failed to invoke remote method: " + invocation.getMethodName() + ", provider: " + getUrl() + ", cause: " + e.getMessage(), e);
        }
    }



    //---------------------

    public void asyncCall(String serviceMethodName,
                          Object request,
                          RPCCallback callback) {
        String[] splitArray = serviceMethodName.split("\\.");
        if (splitArray.length != 2) {
            log.error("serviceMethodName={} is not valid", serviceMethodName);
            return;
        }
        String serviceName = splitArray[0];
        String methodName = splitArray[1];
        if (callback == null) {
            log.error("callback of async call can not be null");
            throw new IllegalArgumentException("callback of async call can not be null");
        }
        Type type = callback.getClass().getGenericInterfaces()[0];
        Class responseBodyClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        final String logId = UUID.randomUUID().toString();
        RPCMessage<RPCHeader.RequestHeader> fullRequest = DefaultFuture.buildFullRequest(
                logId, serviceName, methodName, request, responseBodyClass);
        this.sendRequest(fullRequest, callback);
    }

    public <T> DefaultFuture sendRequest(final RPCMessage<RPCHeader.RequestHeader> fullRequest,
                                         RPCCallback<T> callback) {
        final String logId = fullRequest.getHeader().getLogId();
        try {
            this.doSend(fullRequest);
            // add request to RPCFuture and add timeout task
            final ScheduledExecutorService scheduledExecutor = RPCClient.getScheduledExecutor();
            final long readTimeout = RPCClient.getRpcClientOption().getReadTimeoutMillis();
            final String serviceName = fullRequest.getHeader().getServiceName();
            final String methodName = fullRequest.getHeader().getMethodName();
            ScheduledFuture scheduledFuture = scheduledExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    DefaultFuture rpcFuture = DefaultFuture.(logId);
                    if (rpcFuture != null) {
                        log.debug("request timeout, logId={}, service={}, method={}",
                                logId, serviceName, methodName);
                        rpcFuture.timeout();
                    } else {
                        log.debug("request logId={} not found", logId);
                    }
                }
            }, readTimeout, TimeUnit.MILLISECONDS);

            DefaultFuture future = new DefaultFuture(scheduledFuture, fullRequest, callback);
            DefaultFuture.ad(logId, future);
            return future;
        } catch (RuntimeException ex) {
            DefaultFuture.removeRPCFuture(logId);
            return null;
        }
    }

    private void doSend(RPCMessage<RPCHeader.RequestHeader> fullRequest) {

        Channel channel = CHANNELS.get().getChannel();
        log.debug("channel isActive={}", channel.isActive());
        //connection.setChannel(channel);
        channel.writeAndFlush(fullRequest);
    }*/
}
