package com.tmhp.ssoclient.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tmhp.ssoclient.common.interceptor.SSOInterceptor;

/***
 * @author zqf
 * @date 2018年5月16日
 * 很恶心的现象就是，必须继承过时的WebMvcConfigurerAdapter类才能正常进行dispatcherServlet跳转，
 * 如果继承替代的WebMvcConfigurationSupport类会报错，错误信息如下：
 * Could not resolve view with name '/error/500' in servlet with name 'dispatcherServlet'
 */
@SuppressWarnings("deprecation")
@Configuration
public class SpringMVCConfig extends WebMvcConfigurerAdapter {

    // 单点登录拦截器
    @Bean
    SSOInterceptor SSOInterceptor() {
        return new SSOInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.SSOInterceptor()).addPathPatterns("/**");
    }

}
