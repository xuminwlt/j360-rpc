package me.j360.rpc.client;

import lombok.Data;

@Data
public class RPCClientOption {

    private int connectTimeoutMillis = 100;

    private int readTimeoutMillis = 1000;

    private int writeTimeoutMillis = 100;

    // The keep alive
    private boolean keepAlive = true;

    private boolean reuseAddr = true;

    private boolean tcpNoDelay = true;

    // so linger
    private int soLinger = 5;

    // backlog
    private int backlog = 100;

    // receive buffer size
    private int receiveBufferSize = 1024 * 64;

    // send buffer size
    private int sendBufferSize = 1024 * 64;

    // keepAlive时间（second）
    private int keepAliveTime;

    // acceptor threads, default use Netty default value
    private int acceptorThreadNum = 0;

    // io threads, default use Netty default value
    private int ioThreadNum = 0;

    // The max size
    private int maxSize = Integer.MAX_VALUE;

}
