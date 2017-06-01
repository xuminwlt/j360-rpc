package me.j360.rpc.server.filter;

import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;
import me.j360.rpc.core.Filter;
import me.j360.rpc.core.FilterChain;
import me.j360.rpc.core.FilterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public class ServerInvokeFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(ServerInvokeFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

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

    @Override
    public void destroy() {

    }
}
