package com.bjpowernode.crm.base.interceptor;

import com.bjpowernode.crm.settings.bean.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        //url:全球资源统一定位
        StringBuffer requestURL = request.getRequestURL();
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            //重定向到登录页面
            response.sendRedirect("/crm/login.jsp");
            return false;
        }
        //true:放行
        return true;
    }
}
