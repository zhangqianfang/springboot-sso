package com.tmhp.ssoserver.web.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tmhp.ssoserver.domain.SSOSession;
import com.tmhp.ssoserver.domain.User;
import com.tmhp.ssoserver.service.SessionService;
import com.tmhp.ssoserver.service.UserService;
import com.tmhp.ssoserver.util.Md5Util;
import com.tmhp.ssoserver.util.VerifyCodeUtil;

/***
 * @author zqf
 * @date 2018年5月9日
 * 
 */
@Controller
@RequestMapping("/sso")
public class SSOServerLogin {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    /***
     * 用户登录
     * @param user
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/login")
    public void login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        String clientUrl = request.getParameter("clientUrl");
        if (StringUtils.isEmpty(clientUrl)) {
            clientUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }
        String verifyCode = (String) request.getSession().getAttribute(VerifyCodeUtil.SESSION_SECURITY_CODE);
        // 验证码校验
        if (verifyCode != null) {
            if (verifyCode.equals(user.getVerifyCode())) {
                user.setPassword(Md5Util.str2MD5(user.getPassword())); // 密码MD5加密
                if (!this.userService.validatePassword(user)) {
                    response.getWriter().print("{\"success\":false,\"loginError\":\"" + "账号或密码有误" + "\"}");
                    return;
                }
                User userinfo = this.userService.selectByUsername(user.getUsername());
                // 当前账号已有其它session登录过，则删除原账号全局session，保证单一登录
                if (this.sessionService.containsUser(userinfo.getUsername())) {
                    this.sessionService.removeSessionByUsername(userinfo.getUsername());
                }
                SSOSession session = new SSOSession(request.getSession().getId(), userinfo, request.getRequestURI());
                // 创建全局session
                this.sessionService.createSession(session);
                response.getWriter().print("{\"success\":true, \"clientUrl\":\"" + clientUrl + "\", \"loginError\":\"\"}");
                return;
            } else {
                response.getWriter().print("{\"success\":false, \"loginError\":\"验证码不正确\"}");
                return;
            }
        } else {
            response.getWriter().print("{\"success\":false,\"loginError\":\"验证码为空\"}");
            return;
        }
    }

    /***
     * 生成登录验证码
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.GET)
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        // 设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 为了手机客户端方便使用数字验证码
        String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_ONLY, 4, null);
        request.getSession().setAttribute(VerifyCodeUtil.SESSION_SECURITY_CODE, verifyCode);
        try {
            // 设置输出的内容的类型为JPEG图像
            response.setContentType("image/jpeg");
            BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null, null);
            // 写给浏览器
            ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
