package com.tmhp.ssoserver.service;

import com.tmhp.ssoserver.domain.User;

/***
 * @author zqf
 * @date 2018年5月16日
 * 
 */
public interface UserService {

    /**
     * 验证用户名密码是否正确
     * @param user
     * @return
     */
    boolean validatePassword(User user);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User selectByUsername(String username);
}
