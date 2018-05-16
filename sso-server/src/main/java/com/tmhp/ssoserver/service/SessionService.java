package com.tmhp.ssoserver.service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tmhp.ssoserver.domain.SSOSession;
import com.tmhp.ssoserver.domain.User;

/***
 * 单点登录全局session服务类
 * @author zqf
 * @date 2018年5月9日
 * 
 */
@Service
public class SessionService {

    // 通过静态Map存储全局session信息
    private static Map<String, SSOSession> sessionMap = new ConcurrentHashMap<String, SSOSession>();
    /* 默认24小时清除session */
    private static final Integer SESSION_TIMER = 24 * 3600;

    static {
        refreshSession();
    }

    /**
     * 创建全局session信息
     * @param session
     */
    public void createSession(SSOSession session) {
        sessionMap.put(session.getSessionId(), session);
    }

    /**
     * 删除全局session信息
     * @param sessionId
     */
    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    /**
     * 根据用户名删除该用户名已登录的全局session信息，用于单一登录
     * @param username
     */
    public void removeSessionByUsername(String username) {
        sessionMap.values().iterator().forEachRemaining(session -> {
            if (username.equals(session.getUsername())) {
                sessionMap.remove(session.getSessionId());
                return;
            }
        });
    }

    /**
     * 验证全局sessionId是否存在
     * @param sessionId
     * @return
     */
    public boolean validateBySessionId(String sessionId) {
        return (sessionId != null) && sessionMap.containsKey(sessionId);
    }

    /**
     * 验证用户名是否已经登录
     * @param username
     * @return
     */
    public boolean containsUser(String username) {
        return sessionMap.values().stream().map(s -> s.getUsername()).collect(Collectors.toList()).contains(username);
    }

    /**
     * 根据全局sessionId获取用户名
     * @param sessionId
     * @return
     */
    public String getUsernameBySessionId(String sessionId) {
        if ((sessionId != null) && sessionMap.containsKey(sessionId)) {
            return sessionMap.get(sessionId).getUsername();
        } else {
            return "";
        }
    }

    /**
     * 根据全局sessionId获取用户信息
     * @param sessionId
     * @return
     */
    public User getUserinfoBySessionId(String sessionId) {
        if ((sessionId != null) && sessionMap.containsKey(sessionId)) {
            return sessionMap.get(sessionId).getUserinfo();
        } else {
            return null;
        }
    }

    /**
     * 根据用户名获取客户端Url
     * @param username
     * @return
     */
    public String getClientUrlByUsername(String username) {
        for (SSOSession session : sessionMap.values()) {
            if (username.equals(session.getUsername())) {
                return session.getClientUrl();
            }
        }
        return "";
    }

    // 每隔2秒刷新session，清除过期的session信息
    private static void refreshSession() {
        new Thread() { // 使用线程，否则会造成spring加载bean时卡住无法启动JVM
            @Override
            public void run() {
                while (true) {
                    Iterator<SSOSession> iterator = sessionMap.values().iterator();
                    SSOSession session = null;
                    while (iterator.hasNext()) {
                        session = iterator.next();
                        if (((System.currentTimeMillis() / 1000) - session.getTime()) > SESSION_TIMER) {
                            sessionMap.remove(session.getSessionId());
                        }
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
