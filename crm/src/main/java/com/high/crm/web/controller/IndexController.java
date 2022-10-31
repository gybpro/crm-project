package com.high.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname IndexController
 * @Description 首页跳转
 * @Author high
 * @Create 2022/10/29 20:57
 * @Version 1.0
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index() {
        // 请求转发
        return "index";
    }
}
