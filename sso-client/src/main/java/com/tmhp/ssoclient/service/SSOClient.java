package com.tmhp.ssoclient.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.tmhp.ssoclient.common.constants.SessionConstants;
import com.tmhp.ssoclient.util.HttpClientUtil;
import com.tmhp.ssoserver.domain.User;

/***
 * @author zqf
 * @date 2018年5月9日
 * 
 */
@Component
public class SSOClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /* 单点登录服务器地址 */
    @Value("${ssoServerUrl}")
    private String ssoServerUrl;

    /***
     * 客户端验证全局session是否存在
     * @param request
     * @return
     * @throws Exception
     */
    public boolean checkLogin(HttpServletRequest request) throws Exception {
        JSONObject json = new JSONObject();
        json.put("sessionId", request.getSession().getAttribute(SessionConstants.SSOSERVER_SESSIONID));
        String result = HttpClientUtil.sendHttpPost(this.ssoServerUrl + "/sso/loginCheck", json.toJSONString());
        JSONObject resultJson = JSONObject.parseObject(result);
        this.logger.debug("验证全局session：" + resultJson.getBoolean("success"));
        return resultJson.getBoolean("success");
    }

    /***
     * 客户端接收ticket并且进行验证
     * @param request
     * @return
     * @throws Exception
     */
    public boolean checkTicket(HttpServletRequest request) throws Exception {
        JSONObject json = new JSONObject();
        json.put("ticket", request.getParameter("ticket"));
        String result = HttpClientUtil.sendHttpPost(this.ssoServerUrl + "/sso/ticketCheck", json.toJSONString());
        JSONObject resultJson = JSONObject.parseObject(result);
        if (resultJson.getBoolean("success")) { // 验证票据成功
            this.logger.info("验证票据成功！！！");
            // 将用户信息和单点登录全局sessionId存入客户端session中
            if (request.getSession().getAttribute(SessionConstants.USERINFO) == null) {
                request.getSession().setAttribute(SessionConstants.USERINFO, resultJson.getJSONObject("data").getObject("userinfo", User.class));
                request.getSession().setAttribute(SessionConstants.SSOSERVER_SESSIONID, resultJson.getJSONObject("data").getString("sessionId"));
            }
            return true;
        } else {
            this.logger.info("验证票据失败！！！");
            return false;
        }
    }

    /***
     * 获取单点登录服务器地址
     * @return
     */
    public String getSSOServerURL() {
        return this.ssoServerUrl;
    }

}