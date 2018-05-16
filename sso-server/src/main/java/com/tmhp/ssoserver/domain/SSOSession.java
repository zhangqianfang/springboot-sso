package com.tmhp.ssoserver.domain;

import java.io.Serializable;

/***
 * 记录单点登录全局session信息
 * @author zqf
 * @date 2018年5月9日
 * 
 */
public class SSOSession implements Serializable {

    private static final long serialVersionUID = -2408053763676915702L;

    /* 全局sessionId */
    private String sessionId;
    /* 用户名 */
    private String username;
    /* 用户信息 */
    private User userinfo;
    /* 客户端URL */
    private String clientUrl;
    /* 全局sessionId生成时间 */
    private final long time = System.currentTimeMillis() / 1000;

    public SSOSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public SSOSession(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
    }

    public SSOSession(String sessionId, User userinfo) {
        this.sessionId = sessionId;
        this.username = userinfo == null ? "" : userinfo.getUsername();
        this.userinfo = userinfo;
    }

    public SSOSession(String sessionId, User userinfo, String clientUrl) {
        this.sessionId = sessionId;
        this.username = userinfo == null ? "" : userinfo.getUsername();
        this.userinfo = userinfo;
        this.clientUrl = clientUrl;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUserinfo() {
        return this.userinfo;
    }

    public void setUserinfo(User userinfo) {
        this.userinfo = userinfo;
    }

    public String getClientUrl() {
        return this.clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public long getTime() {
        return this.time;
    }

}
