package me.j360.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.j360.rpc.codec.RPCHeader;
import me.j360.rpc.codec.RPCMessage;
import me.j360.rpc.server.RPCServer;
import me.j360.rpc.server.RPCServiceCallManager;
import me.j360.rpc.server.RPCServiceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Package: me.j360.rpc.server.handler
 * User: min_xu
 * Date: 2017/5/19 上午10:17
 * 说明：
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<RPCMessage<RPCHeader.RequestHeader>> {

    private static final Logger LOG = LoggerFactory.getLogger(RPCServerHandler.class);

    private RPCServer rpcServer;

    public RPCServerHandler(RPCServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             RPCMessage<RPCHeader.RequestHeader> request) throws Exception {
        RPCServiceTask task = new RPCServiceTask(ctx.channel(), request, rpcServer);
        RPCServiceCallManager.execute(task);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        RPCMessage<RPCHeader.ResponseHeader> response = new RPCMessage<>();
        RPCHeader.ResponseHeader header = RPCHeader.ResponseHeader.newBuilder()
                .setResCode(RPCHeader.ResCode.RES_FAIL).setResMsg(cause.getMessage()).build();
        response.setHeader(header);
        response.setBody(new byte[]{});
        ctx.fireChannelRead(response);
    }
}