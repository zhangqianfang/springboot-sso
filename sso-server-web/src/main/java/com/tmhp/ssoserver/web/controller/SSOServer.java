package com.tmhp.ssoserver.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tmhp.ssoserver.domain.Result;
import com.tmhp.ssoserver.domain.User;
import com.tmhp.ssoserver.service.SessionService;
import com.tmhp.ssoserver.service.TicketService;

/***
 * 
 * @author zqf
 * @date 2018年5月9日
 */
@Controller
@RequestMapping(value = "/sso")
public class SSOServer {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private TicketService ticketService;

    /***
     * 单点登录验证，判断用户是否登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/ssoServer")
    private String ssoServer(HttpServletRequest request, HttpServletResponse response) {
        String clientUrl = request.getParameter("clientUrl");
        HttpSession session = request.getSession();
        session.setAttribute("clientUrl", clientUrl);
        if (session.getId() != null) {
            // 判断用户是否登录，已登录则携带票据直接返回
            if (this.sessionService.validateBySessionId(session.getId())) {
                return "redirect:" + clientUrl + "?ticket=" + this.ticketService.creatTicket(session.getId());
            } else { // 未登录，重定向到登录页（可以是客户端登录页，也可以是服务端登录页）
                request.setAttribute("clientUrl", clientUrl + "?ticket=" + this.ticketService.creatTicket(session.getId()));
                return "login";
            }
        }
        return "error/404";
    }

    /***
     * 验证全局session
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/loginCheck")
    private Result loginCheck(@RequestBody String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String sessionId = jsonObject.getString("sessionId");
        if (this.sessionService.validateBySessionId(sessionId)) {
            return new Result(true, 1000, "全局session验证成功.", null);
        } else {
            return new Result(false, 500, "全局session验证失败.", null);
        }
    }

    /***
     * 票据验证
     * @param ticket
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ticketCheck", method = RequestMethod.POST)
    public Result ticketCheck(@RequestBody String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String ticket = jsonObject.getString("ticket");
        User userinfo = this.ticketService.getUserinfo(ticket);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userinfo", userinfo);
        resultMap.put("sessionId", this.ticketService.getSessionId(ticket)); // 将全局sessionId发到客户端缓存
        if (this.ticketService.validateTicket(ticket)) {
            this.ticketService.removeTicket(ticket); // 票据验证完销毁
            return new Result(true, 1000, "票据验证成功.", resultMap);
        } else {
            return new Result(false, 500, "票据验证失败.", null);
        }
    }

    /***
     * 用户登出
     * @param request
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        String clientUrl = request.getParameter("clientUrl");
        if (StringUtils.isEmpty(clientUrl)) {
            clientUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }
        HttpSession session = request.getSession();
        session.setAttribute("clientUrl", clientUrl);
        if (session.getId() != null) {
            // 删除全局session
            User userinfo = this.sessionService.getUserinfoBySessionId(request.getSession().getId());
            this.sessionService.removeSession(request.getSession().getId());
            System.out.println("用户" + userinfo + "登出成功！");
            request.setAttribute("clientUrl", clientUrl + "?ticket=" + this.ticketService.creatTicket(session.getId()));
            return "login";
        }
        return "/error/404";
    }

}