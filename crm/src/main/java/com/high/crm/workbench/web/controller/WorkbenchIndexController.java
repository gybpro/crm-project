package com.high.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;

/**
 * @Classname WorkbenchIndexController
 * @Description 业务首页跳转
 * @Author high
 * @Create 2022/10/31 11:03
 * @Version 1.0
 */
@Controller
public class WorkbenchIndexController extends HttpServlet {
    @RequestMapping("/workbench/index.do")
    public String index() {
        return "workbench/index";
    }
}
