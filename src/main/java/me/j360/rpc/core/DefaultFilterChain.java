package me.j360.rpc.core;

import me.j360.rpc.codec.protostuff.RpcRequest;
import me.j360.rpc.codec.protostuff.RpcResponse;

import java.util.Iterator;
import java.util.List;

public abstract class DefaultFilterChain implements FilterChain {

    protected List<Filter> filters;
    protected Iterator<Filter> iterator;
    protected RpcRequest fullRequest;
    protected RpcResponse fullResponse;

    public void doFilter(RpcRequest fullRequest,
                  RpcResponse fullResponse) {
        if (fullRequest == null) {
            throw new IllegalArgumentException("fullRequest == null");
        }
        if (this.iterator == null) {
            this.iterator = this.filters.iterator();
        }
        if (this.iterator.hasNext()) {
            Filter nextFilter = this.iterator.next();
            nextFilter.doFilter(fullRequest, fullResponse, this);
        }
        this.fullRequest = fullRequest;
        this.fullResponse = fullResponse;
    }

    public RpcRequest getFullRequest() {
        return fullRequest;
    }

    public RpcResponse getFullResponse() {
        return fullResponse;
    }

}
