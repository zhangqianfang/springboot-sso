package com.tmhp.ssoserver.web.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 * 
 * @author zqf
 * @date 2018年5月3日
 * 
 * @RestController相当于@Controller+@ResponseBody(每一个方法上默认返回的是json串)
 */
@Controller
public class HelloController {

    @RequestMapping("")
    public String index() {
        return "index";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

}
