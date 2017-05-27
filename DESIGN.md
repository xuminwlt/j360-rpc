rpc设计的一些简单原理

## why

源于阿里的馈赠,这么多年一直使用dubbo作为首选的rpc框架,得益于广大的使用量,稳定性是压倒一切的,随着业务的发展,rpc作为分布式架构中的基础框架需要不断的扩展,dubbo是否能够很好的支撑这种扩展也表现除了良好的兼容性,但同时扩展部分互相的影响越来越大,这时只有深刻了解RPC内部的原理才能从根本上解决rpc和其他框架间的隔离性、一致性问题,基于种种原因,还是基于自己对RPC的理解简单对rpc的设计总结一番。最后再抛出一系列可能存在的问题。

## what

rpc远程服务调用,基于java的远程服务调用,首先能想到的功能和可能需要用到的技术会有哪些?

### Server技术栈

- 服务注册 
- 请求传输应答
- 任务分派
- 服务调用
- Spring容器支持
- 扩展支持

### Client技术栈

- 服务发现
- 动态代理
- 请求调用封装
- 同步异步
- 连接管理
- Spring容器支持
- 扩展支持

## how

```sequence
title: RPC请求时序图

participant Client as C-alias
participant Server as S-alias

Note over C: 方法调用接口代理方法
Note over C: 封装接口方法到对象Request

C->S: Netty编码Request并flush到Server

Note over S: 接收Request对象,解码
Note over S: 根据接口信息通过反射执行实现类方法,封装并返回Response

S-->C: Netty编码Response并flush到Client

Note over C: 接收Response解码得到结果Result

```

针对请求时序中的每个细节,这里一一列举:

### 前置功能-服务注册:Server
### 前置功能-服务启动:Server


### 容器启动:Client

### 服务发现:Client

### 服务绑定:Client

### 服务请求主动发起:Client

### 服务路由选择:Client

### 请求封装编码:Client

### 请求传输:Client

### 请求阻塞:Client

### 请求接收:Server

### 请求解码:Server

### 执行Task封装:Server

### Task执行:Server

### Task寻找注册服务表:Server

### 执行服务:Server

### 封装结果Response:Server

### 请求返回编码Response:Server

### 接收Response解码:Client

### 完成阻塞:Client

### 同步异步判断:Client

### 封装结果:Client

### 返回给请求方法:Client

