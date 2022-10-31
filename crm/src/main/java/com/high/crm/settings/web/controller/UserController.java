package com.high.crm.settings.web.controller;

import com.high.crm.commons.constant.Constant;
import com.high.crm.commons.domain.ResultDTO;
import com.high.crm.commons.util.DateUtil;
import com.high.crm.settings.domain.User;
import com.high.crm.settings.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname UserController
 * @Description settings下用户相关访问控制
 * @Author high
 * @Create 2022/10/29 21:44
 * @Version 1.0
 */
@Controller
// 类URL与资源目录保持一致
@RequestMapping("/settings/qx/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 方法URL与方法名保持一致，.do和.action都是规范，各公司不一样，没有规定也可不加
    @RequestMapping("/toLogin.do")
    public String toLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String loginAct = "";
            String loginPwd = "";
            for (Cookie cookie : cookies) {
                if ("loginAct".equals(cookie.getName())) {
                    loginAct = cookie.getValue();
                }
                if ("loginPwd".equals(cookie.getName())) {
                    loginPwd = cookie.getValue();
                }
            }
            Map<String, String> map = new HashMap<>();
            map.put("loginAct", loginAct);
            map.put("loginPwd", loginPwd);
            User user = userService.selectUserByLoginActAndLoginPwd(map);
            if (user != null) {
                String nowDateTime = DateUtil.formatDateTime(new Date());
                if (nowDateTime.compareTo(user.getExpireTime()) < 0 || !("0".equals(user.getLockState())) || user.getAllowIps().contains(request.getRemoteAddr())) {
                    // 获取session对象
                    HttpSession session = request.getSession();
                    // 将用户信息存入会话
                    session.setAttribute("user", user);
                    return "redirect:/workbench/index.do";
                }
            }
        }
        // 请求转发到登录页面
        // 前端的路径前面都不能加'/'，加'/'会默认为从服务进程的根开始，不会带项目名或指定路径
        return "settings/qx/user/login";
    }

    @ResponseBody
    @RequestMapping("/login.do")
    public ResultDTO login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request,
                           HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.selectUserByLoginActAndLoginPwd(map);
        ResultDTO resultDTO = new ResultDTO();
        if (user == null) {
            resultDTO.setCode(Constant.RETURN_DTO_CODE_FAIL);
            resultDTO.setMessage("用户名密码错误");
        } else {
            String nowDateTime = DateUtil.formatDateTime(new Date());
            if (nowDateTime.compareTo(user.getExpireTime()) > 0) {
                resultDTO.setCode(Constant.RETURN_DTO_CODE_FAIL);
                resultDTO.setMessage("用户已过期");
            } else if ("0".equals(user.getLockState())) {
                resultDTO.setCode(Constant.RETURN_DTO_CODE_FAIL);
                resultDTO.setMessage("用户状态被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                resultDTO.setCode(Constant.RETURN_DTO_CODE_FAIL);
                resultDTO.setMessage("ip受限");
            } else {
                resultDTO.setCode(Constant.RETURN_DTO_CODE_SUCCESS);
                resultDTO.setMessage("登录成功");
                // 获取session对象
                HttpSession session = request.getSession();
                // 将用户信息存入会话
                session.setAttribute("user", user);
                if ("true".equals(isRemPwd)) {
                    Cookie c1 = new Cookie("loginAct", loginAct);
                    c1.setMaxAge(10 * 24 * 60 * 60 * 1000);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", loginPwd);
                    c2.setMaxAge(10 * 24 * 60 * 60 * 1000);
                    response.addCookie(c2);
                } else {
                    Cookie c1 = new Cookie("loginAct", "");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return resultDTO;
    }

    @RequestMapping("/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        Cookie c1 = new Cookie("loginAct", "");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "");
        c2.setMaxAge(0);
        response.addCookie(c2);
        session.invalidate();
        return "redirect:/";
    }

}
