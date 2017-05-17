package me.j360.rpc.core;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Package: me.j360.rpc.core
 * User: min_xu
 * Date: 2017/5/17 下午1:57
 * 说明：
 */

@Data
@AllArgsConstructor
public class ProviderConnection {

    private IpPort ipPort;
    private Channel channel;
}
