package com.tmhp.ssoserver.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmhp.ssoserver.domain.User;

/***
 * 票据服务类
 * @author zqf
 * @date 2018年5月9日
 * 
 */
@Service
public class TicketService {

    @Autowired
    private SessionService sessionService;
    // 通过静态Map保存票据信息和其对应的全局sessionId，票据信息只验证一次成功后即销毁
    private static Map<String, String> ticketsMap = new ConcurrentHashMap<String, String>();

    /**
     * 通过sessionId生成票据信息
     * @param sessionId
     * @return
     */
    public String creatTicket(String sessionId) {
        String ticket = getTicket();
        ticketsMap.put(ticket, sessionId);
        return ticket;
    }

    /**
     * 验证令牌和全局sessionId
     * @param ticket
     * @return
     */
    public boolean validateTicket(String ticket) {
        return ticketsMap.containsKey(ticket) && this.sessionService.validateBySessionId(ticketsMap.get(ticket));
    }

    /**
     * 票据验证成功后移除
     * @param ticket
     */
    public void removeTicket(String ticket) {
        ticketsMap.remove(ticket);
    }

    /**
     * 根据票据获取全局sessionId
     * @param ticket
     * @return
     */
    public String getSessionId(String ticket) {
        return ticketsMap.get(ticket);
    }

    /**
     * 根据票据获取用户名
     * @param ticket
     * @return
     */
    public String getUsername(String ticket) {
        return this.sessionService.getUsernameBySessionId(ticketsMap.get(ticket));
    }

    /**
     * 根据票据获取用户信息
     * @param ticket
     * @return
     */
    public User getUserinfo(String ticket) {
        return this.sessionService.getUserinfoBySessionId(ticketsMap.get(ticket));
    }

    // 通过UUID生成随机票据
    private static String getTicket() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
