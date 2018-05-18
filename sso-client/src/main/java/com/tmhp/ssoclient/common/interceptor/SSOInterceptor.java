package com.tmhp.ssoclient.common.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tmhp.ssoclient.common.constants.SessionConstants;
import com.tmhp.ssoclient.service.SSOClient;
import com.tmhp.ssoserver.domain.User;

/***
 * 单点登录客户端拦截器
 * @author zqf
 * @date 2018年5月16日
 */
public class SSOInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SSOClient ssoClient;

    /***
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.logger.debug("进入SSO拦截器 url:" + request.getRequestURL());
        User userinfo = (User) request.getSession().getAttribute(SessionConstants.USERINFO);
        // 验证客户端session和单点登录全局session中用户是否登录
        if ((userinfo != null) && this.ssoClient.checkLogin(request)) {
            return true;
        } else if (!StringUtils.isEmpty(request.getParameter("ticket"))) { // SSO服务端发送回来带票据的请求
            boolean flag = this.ssoClient.checkTicket(request);
            if (flag) {
                return true;
            } else { // 票据验证不通过，跳转单点登录服务端
                return this.jumpToSSOServer(request, response);
            }
        } else {
            return this.jumpToSSOServer(request, response);
        }
    }

    /***
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (response.getStatus() == 500) {
            modelAndView.setViewName("/error/500");
        } else if (response.getStatus() == 404) {
            modelAndView.setViewName("/error/404");
        }
    }

    /***
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {

    }

    private boolean jumpToSSOServer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String clientUrl = request.getRequestURL().toString();
        this.logger.info("用户未登录或票据验证失败，跳转SSOServer单点登录服务端(" + this.ssoClient.getSSOServerURL() + "/sso/ssoServer?clientUrl=" + clientUrl + ")！请求地址："
                + request.getRequestURL());
        response.sendRedirect(this.ssoClient.getSSOServerURL() + "/sso/ssoServer?clientUrl=" + clientUrl);
        return false;
    }

}
