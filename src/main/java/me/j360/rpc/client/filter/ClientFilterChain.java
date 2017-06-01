package me.j360.rpc.client.filter;

import me.j360.rpc.client.RPCClient;
import me.j360.rpc.core.DefaultFilterChain;
import me.j360.rpc.core.Filter;

import java.util.ArrayList;
import java.util.List;

public class ClientFilterChain extends DefaultFilterChain {

    public ClientFilterChain(List<Filter> filters, RPCClient rpcClient) {
        this.filters = new ArrayList<>();
        if (filters != null && filters.size() > 0) {
            this.filters.addAll(filters);
        }
        this.filters.add(new ClientInvokeFilter());
        for (Filter filter : this.filters) {
            if (AbstractClientFilter.class.isAssignableFrom(filter.getClass())) {
                ((AbstractClientFilter) filter).setRPCClient(rpcClient);
            }
        }
    }
}
