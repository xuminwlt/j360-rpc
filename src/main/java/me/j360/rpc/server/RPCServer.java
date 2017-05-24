package me.j360.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import me.j360.rpc.codec.protostuff.RpcDecoder;
import me.j360.rpc.codec.protostuff.RpcEncoder;
import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;
import me.j360.rpc.core.Endpoint;
import me.j360.rpc.register.ServiceRegister;
import me.j360.rpc.server.handler.RPCServerHandler;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/17 下午12:20
 * 说明：
 */

@Slf4j
public class RPCServer implements Endpoint {


    private RPCServerOption rpcServerOption;
    private ServiceRegister serviceRegister;

    public  Map<String, Object> handlerMap;

    private InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1",20880);


    public RPCServer(RPCServerOption rpcServerOption, ServiceRegister serviceRegister,Map<String, Object> handlerMap) {
        this.rpcServerOption = rpcServerOption;
        this.serviceRegister = serviceRegister;
        this.handlerMap = handlerMap;
    }



    /**
     * 启动NettyServer
     * 执行注册
     * 启动Server执行器
     *
     */
    public void start() {
        startServer();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }


    public void shutdown() {

    }


    private void startServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0))
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(new RPCServerHandler(handlerMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            registerZk();

            startTaskExecuter();

            ChannelFuture future = bootstrap.bind(socketAddress.getHostName(),socketAddress.getPort()).sync();
            log.debug("Server started on port {}", socketAddress.getPort());

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void registerZk() {
        if (serviceRegister != null) {
            for (String interfaceName:handlerMap.keySet()) {
                serviceRegister.register(interfaceName,socketAddress.getHostName()+":"+socketAddress.getPort());
            }

        }
    }

    private void startTaskExecuter(){
        RPCServiceCallManager.init();
    }

}
