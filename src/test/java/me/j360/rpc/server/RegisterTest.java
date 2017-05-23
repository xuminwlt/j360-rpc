package me.j360.rpc.server;

import me.j360.rpc.register.ServiceRegister;
import org.junit.Test;

/**
 * Package: me.j360.rpc.server
 * User: min_xu
 * Date: 2017/5/23 下午11:10
 * 说明：
 */

public class RegisterTest {

    @Test
    public void registerTest() {
        ServiceRegister serviceRegister = new ServiceRegister("localhost:2181");
        serviceRegister.register("me.j360.rpc");
    }
}
