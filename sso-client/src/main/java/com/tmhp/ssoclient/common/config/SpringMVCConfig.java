package com.tmhp.ssoclient.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.tmhp.ssoclient.common.interceptor.SSOInterceptor;

/***
 * @author zqf
 * @date 2018年5月16日
 * 
 */
@SpringBootConfiguration
public class SpringMVCConfig extends WebMvcConfigurationSupport {

    @Autowired
    private SSOInterceptor ssoInterceptor; // 单点登录拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.ssoInterceptor).addPathPatterns("/**").excludePathPatterns("/static");
    }
}
