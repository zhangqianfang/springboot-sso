package com.tmhp.ssoserver.service;

import org.springframework.stereotype.Service;

import com.tmhp.ssoserver.domain.User;

/***
 * 用户信息服务类
 * @author zqf
 * @date 2018年5月16日
 * 
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public boolean validatePassword(User user) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public User selectByUsername(String username) {
        // TODO Auto-generated method stub
        return new User("admin", "123456");
    }

}
