package me.j360.rpc.service;

import me.j360.rpc.model.UserDO;
import me.j360.rpc.spring.RpcService;

/**
 * Package: me.j360.rpc.service
 * User: min_xu
 * Date: 2017/5/25 下午7:03
 * 说明：
 */

@RpcService(value = UserService.class)
public class UserServiceImpl implements UserService {

    @Override
    public UserDO getUser(Long uid) {
        UserDO userDO = new UserDO();
        userDO.setUid(uid);
        userDO.setName(String.valueOf(uid));
        return userDO;
    }
}
