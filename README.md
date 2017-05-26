# j360-rpc

lite rpc for java

## lite风

- 注册中心服务发现
- 动态代理
- netty
- 同步/异步
- Filter扩展
- Spring扩展
- 服务自动注入

具体设计细节: [在这里--->](DESIGN.md)

## 使用

1. 定义接口

```
public interface UserService {
    
    /**
     * 获取用户基本信息
     * @param uid
     * @return
     */
    UserDO getUser(Long uid);
}
```

2. 定义实现

```
public class UserServiceImpl implements UserService {

    @Override
    public UserDO getUser(Long uid) {
        UserDO userDO = new UserDO();
        userDO.setUid(uid);
        userDO.setName(String.valueOf(uid));
        return userDO;
    }
}
```

3. 选择你的环境

#### native mode

```
什么都不要做
```

#### spring env

添加服务自动发现的注解

```

@RpcService(value = UserService.class)
public class UserServiceImpl implements UserService {
```

4. 启动Service

#### native mode

```
 @Test
    public void serverStartTest() {
        Map<String,Object> handlerMap = Maps.newHashMap();
        RPCServerOption rpcServerOption = new RPCServerOption();
        ServiceRegister serviceRegister = new ServiceRegister("localhost:2181");
        handlerMap.put(UserService.class.getCanonicalName(),new UserServiceImpl());
        RPCServer rpcServer = new RPCServer(rpcServerOption,serviceRegister,handlerMap);
        rpcServer.start();
    }
```

#### spring env

添加服务自动发现的注解

```
   AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(RPCServerConfiguration.class);
   ctx.start();
```


5. 测试client

#### native mode

```
    @Test
    public void clientTest() {

        UserService userService = rpcClient.create(UserService.class);
        UserDO userDO = userService.getUser(1L);

        System.out.println(userDO.toString());
    }

    @Test
    public void clientAsyncTest() throws InterruptedException {
        RPCCallback<UserDO> rpcCallback = new RPCCallback<UserDO>() {
            @Override
            public void success(UserDO response) {
                System.out.println(response.toString());
            }

            @Override
            public void fail(Throwable e) {

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserService userService = rpcClient.createAsync(UserService.class, rpcCallback);
                userService.getUser(1L);

            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
    }
```

#### spring env

```
public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(RPCClientConfiguration.class);
        ctx.start();

        UserService userService = (UserService) ctx.getBean("userService");
        //Sync
        UserDO userDO = userService.getUser(1L);
        System.out.println(userDO.toString());

        //not support Async

    }

```

## 后话

### v1.0.0

j360系列一直是我作为实验环境验证和测试框架和原型编写的,其中有不少和rpc密切相关的内容,比如链路分析、tcc、分布式id生成器等,一直在基于dubbo做这方面的测试,太重太不好调试,于是考虑将rpc中的所有功能都精简一次,1,0正式版完成.

j360-rpc参考了几个比较热门的rpc项目,在重复造轮子方面没有完全自建,保留原作者签名,同时根据平常的使用习惯增加了诸多细节功能.

### v1 ++

后续会根据需要增加可配置的Filter,便于链路分析等场景,在Resquest、Response增加Header,参考Servlet







