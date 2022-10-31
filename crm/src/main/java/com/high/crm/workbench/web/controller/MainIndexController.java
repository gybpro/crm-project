package com.high.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;

/**
 * @Classname MainIndexController
 * @Description 工作台主窗口显示
 * @Author high
 * @Create 2022/10/31 20:59
 * @Version 1.0
 */
@Controller
@RequestMapping("/workbench/main")
public class MainIndexController extends HttpServlet {
    @RequestMapping("/index.do")
    public String index() {
        return "/workbench/main/index";
    }
}
