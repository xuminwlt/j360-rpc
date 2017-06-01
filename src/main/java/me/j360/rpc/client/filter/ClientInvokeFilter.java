package me.j360.rpc.client.filter;


import me.j360.rpc.client.RPCClient;
import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;
import me.j360.rpc.core.FilterChain;

@SuppressWarnings("unchecked")
public class ClientInvokeFilter extends AbstractClientFilter {

    private RPCClient rpcClient;

    public void setRPCClient(RPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }




    @Override
    public void doFilter(RpcRequest fullRequest,
                         RpcResponse fullResponse,
                         FilterChain chain) {
        try {


        } finally {
            chain.doFilter(fullRequest, fullResponse);
        }
    }

}
