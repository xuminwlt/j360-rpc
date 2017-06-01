package me.j360.rpc.core;

import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;


/**
 * @See javax.servlet.Filter
 */
public interface Filter {

    public void init(FilterConfig filterConfig);

    void doFilter(RpcRequest request,RpcResponse response,
                  FilterChain chain);

    public void destroy();
}
