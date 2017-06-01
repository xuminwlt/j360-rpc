package me.j360.rpc.core;

import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;


public interface FilterChain {

    void doFilter(RpcRequest request,
                  RpcResponse response);
}
