package me.j360.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.core.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Package: me.j360.rpc.client
 * User: min_xu
 * Date: 2017/5/17 上午11:59
 * 说明：
 */

@Slf4j
public class RPCClient implements Endpoint{


    private static RPCClientOption rpcClientOption;
    private static Bootstrap bootstrap;

    /**
     * 因为该rpc只使用单个协议作为传输,所以只需要定义一个Client实例
     * 服务集合内存表,根据依赖的多个服务,一个服务有多个实例
     */
    private static ConcurrentHashMap<String,ServiceProvider> providerMap = new ConcurrentHashMap<>();


    public RPCClient(List<Consumer> consumers,RPCClientOption rpcClientOption) {

        if (rpcClientOption != null) {
            RPCClient.rpcClientOption = rpcClientOption;
        } else {
            RPCClient.rpcClientOption = new RPCClientOption();
        }

        for (Consumer consumer : consumers) {
            addInitProvider(consumer);
        }

        doOpen();

        doConnect();
    }




    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }


    protected void doOpen() {

        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, rpcClientOption.getConnectTimeoutMillis());
        bootstrap.option(ChannelOption.SO_KEEPALIVE, rpcClientOption.isKeepAlive());
        bootstrap.option(ChannelOption.SO_REUSEADDR, rpcClientOption.isReuseAddr());
        bootstrap.option(ChannelOption.TCP_NODELAY, rpcClientOption.isTcpNoDelay());
        bootstrap.option(ChannelOption.SO_RCVBUF, rpcClientOption.getReceiveBufferSize());
        bootstrap.option(ChannelOption.SO_SNDBUF, rpcClientOption.getSendBufferSize());

        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

            }
        };
        bootstrap.group(new NioEventLoopGroup()).handler(initializer);
    }

    protected void doConnect() {

        //连接上所有的服务,如果有必要

    }


    /*public Channel getChannel(String ip,int port) {

    }*/

    private void addInitProvider(Consumer consumer) {
        if (providerMap.containsKey(consumer.getServiceName())) {
            providerMap.get(consumer.getServiceName()).getConnectionMap().put(consumer.getIpPort(),new ProviderConnection(consumer.getIpPort(),null));
        } else {
            ServiceProvider provider = new ServiceProvider();
            Map<IpPort,ProviderConnection> connections = new ConcurrentHashMap<>();
            connections.put(consumer.getIpPort(),new ProviderConnection(consumer.getIpPort(),null));
            provider.setServiceName(consumer.getServiceName());
            provider.setConnectionMap(connections);
            providerMap.put(consumer.getServiceName(),provider);
        }
    }

    private void removeProvider(Consumer consumer) {
        if (providerMap.containsKey(consumer.getServiceName())) {
            //删除connection
            Map<IpPort,ProviderConnection> map = providerMap.get(consumer.getServiceName()).getConnectionMap();
            if (map.containsKey(consumer.getIpPort())) {
                Channel channel = map.get(consumer.getIpPort()).getChannel();
                if (channel != null) {
                    map.get(consumer.getIpPort()).getChannel().close();
                }
            }
            //删除服务
            providerMap.remove(consumer);
        }
    }

    private void addProvider(Consumer consumer) {
        addInitProvider(consumer);

        Map<IpPort,ProviderConnection> map = providerMap.get(consumer.getServiceName()).getConnectionMap();
        if (map.containsKey(consumer.getIpPort())) {
            Channel channel = map.get(consumer.getIpPort()).getChannel();
            if (channel == null) {
                channel = connect(consumer.getIpPort().getIp(),consumer.getIpPort().getPort());
                map.get(consumer.getIpPort()).setChannel(channel);
            }

        }
    }


    public Channel connect(String ip, int port) {
        try {
            final ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        log.debug("Connection {} is established", channelFuture.channel());
                    } else {
                        log.warn(String.format("Connection get failed on {} due to {}",
                                channelFuture.cause().getMessage(), channelFuture.cause()));
                    }
                }
            });
            future.awaitUninterruptibly();
            if (future.isSuccess()) {
                log.debug("connect {}:{} success", ip, port);
                return future.channel();
            } else {
                log.warn("connect {}:{} failed", ip, port);
                return null;
            }
        } catch (Exception e) {
            log.error("failed to connect to {}:{} due to {}", ip, port, e.getMessage());
            return null;
        }
    }

}
