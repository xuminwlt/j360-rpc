package me.j360.rpc.spring;

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

    }

}
