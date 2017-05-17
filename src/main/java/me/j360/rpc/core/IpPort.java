package me.j360.rpc.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Package: me.j360.rpc.core
 * User: min_xu
 * Date: 2017/5/17 下午1:25
 * 说明：
 */

@Data
@AllArgsConstructor
public class IpPort {

    private String ip;
    private int port;
}
