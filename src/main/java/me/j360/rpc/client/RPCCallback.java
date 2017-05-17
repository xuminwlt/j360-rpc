package me.j360.rpc.client;

public interface RPCCallback<T> {

    void success(T response);

    void fail(Throwable e);

}
