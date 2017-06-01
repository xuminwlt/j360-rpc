package me.j360.rpc.client.filter;


import me.j360.rpc.client.RPCClient;
import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;
import me.j360.rpc.core.Filter;
import me.j360.rpc.core.FilterChain;
import me.j360.rpc.core.FilterConfig;

public abstract class AbstractClientFilter implements Filter {

    protected RPCClient rpcClient;

    public void setRPCClient(RPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public abstract void doFilter(RpcRequest fullRequest,
                  RpcResponse fullResponse,
                  FilterChain chain);

    @Override
    public void init(FilterConfig filterConfig) {

    }


    @Override
    public void destroy() {

    }


}
