package me.j360.rpc.server;

import io.netty.channel.Channel;
import me.j360.rpc.codec.RPCMessage;


/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/23 下午2:49
 * 说明：
 */
public class RPCServiceTask implements Runnable{

    private Channel channel;
    private RPCMessage request;
    private Object object;


    @Override
    public void run() {

        byte[] responseBody = (byte[]) object.getClass().getMethod().invoke();
        channel.writeAndFlush(responseBody);

    }
}
