package com.tmhp.ssoclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/***
 * 客户端启动类
 * @author zqf
 * @date 2018年5月16日
 */
@SpringBootApplication
@ComponentScan(value = "com.tmhp")
public class SsoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoClientApplication.class, args);
    }

}
