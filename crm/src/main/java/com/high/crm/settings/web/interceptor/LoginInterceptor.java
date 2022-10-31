package com.high.crm.settings.web.interceptor;

import com.high.crm.commons.constant.Constant;
import com.high.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname LoginInterceptor
 * @Description 登录验证
 * @Author high
 * @Create 2022/10/31 14:36
 * @Version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果用户没有登录成功，则跳转到登录页面
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        if (user == null) {
            response.sendRedirect(request.getContextPath());
            return false;
        }
        return true;
    }
}
