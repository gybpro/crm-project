package com.high.crm.workbench.web.controller;

import com.high.crm.settings.domain.User;
import com.high.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Classname ActivityController
 * @Description 市场活动界面控制
 * @Author high
 * @Create 2022/10/31 21:41
 * @Version 1.0
 */
@Controller
@RequestMapping("/workbench/activity")
public class ActivityController extends HttpServlet {
    private final UserService userService;

    public ActivityController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.selectAllUser();
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/detail.do")
    public String detail() {
        return "workbench/activity/detail";
    }
}
