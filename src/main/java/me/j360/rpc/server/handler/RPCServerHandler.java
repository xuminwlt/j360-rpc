package me.j360.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;
import me.j360.rpc.server.RPCServiceCallManager;
import me.j360.rpc.server.RPCServiceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Package: me.j360.rpc.server.handler
 * User: min_xu
 * Date: 2017/5/19 上午10:17
 * 说明：
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(RPCServerHandler.class);

    private final Map<String, Object> handlerMap;

    public RPCServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             RpcRequest request) throws Exception {
        Object serviceBean = handlerMap.get(request.getClassName());

        RPCServiceTask task = new RPCServiceTask(ctx.channel(), request, serviceBean);
        RPCServiceCallManager.execute(task);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        RpcResponse response = new RpcResponse();
        ctx.fireChannelRead(response);
    }
}