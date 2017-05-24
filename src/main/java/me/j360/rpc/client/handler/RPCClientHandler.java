package me.j360.rpc.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.j360.rpc.client.DefaultFuture;
import me.j360.rpc.codec.protostuff.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * Package: me.j360.rpc.client.handler
 * User: min_xu
 * Date: 2017/5/17 下午6:49
 * 说明：
 */
public class RPCClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(RPCClientHandler.class);

    private volatile Channel channel;
    private SocketAddress remotePeer;

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             RpcResponse response) throws Exception {
        Long requestId = response.getRequestId();
        DefaultFuture future = DefaultFuture.getFuture(requestId);
        if (future == null) {
            LOG.debug("receive msg from server but no request found, logId={}", requestId);
            return;
        }

        if (response.isError()) {
            future.fail(new RuntimeException(response.getError()));
        } else {
            future.success(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error(cause.getMessage(), cause);
        ctx.close();
    }
}
