package me.j360.rpc.spring;

import me.j360.rpc.model.UserDO;
import me.j360.rpc.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Package: me.j360.rpc.spring
 * User: min_xu
 * Date: 2017/5/23 下午5:23
 * 说明：
 */
public class ClientBootstrp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(RPCClientConfiguration.class);
        ctx.start();

        UserService userService = (UserService) ctx.getBean("userService");
        //Sync
        UserDO userDO = userService.getUser(1L);
        System.out.println(userDO.toString());

        //not support Async

    }

}
