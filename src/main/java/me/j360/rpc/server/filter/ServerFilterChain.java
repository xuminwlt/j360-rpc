package me.j360.rpc.server.filter;

import me.j360.rpc.core.DefaultFilterChain;
import me.j360.rpc.core.Filter;

import java.util.ArrayList;
import java.util.List;

public class ServerFilterChain extends DefaultFilterChain {

    public ServerFilterChain(List<Filter> filters) {
        this.filters = new ArrayList<>();
        if (filters != null && filters.size() > 0) {
            this.filters.addAll(filters);
        }
        this.filters.add(new ServerInvokeFilter());
    }

}
