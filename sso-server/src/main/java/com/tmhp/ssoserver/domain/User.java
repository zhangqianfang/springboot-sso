package com.tmhp.ssoserver.domain;

/***
 * 单点登录用户信息，可自行扩展
 * @author zqf
 * @date 2018-5-2
 */
public class User {

    /* 用户ID */
    private Integer userId;
    /* 用户名称 */
    private String username;
    /* 密码 */
    private String password;
    /* 验证码 */
    private String verifyCode;

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return this.verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

}
