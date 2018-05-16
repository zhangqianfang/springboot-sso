package com.tmhp.ssoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.tmhp")
public class SsoServerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoServerWebApplication.class, args);
    }
}
