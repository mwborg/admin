package com.mwb.controller.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fangchen.chai on 2017/4/5.
 */
public class EmployeeInterceptor implements HandlerInterceptor {
    public String[] allowUrls;
//    @Autowired
//    private IEmployeeService employeeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String requestUrl = request.getRequestURI().replace(
                request.getContextPath(), "");
//todo
//        Employee employee = employeeService.getAllEmployee().get(0);
//        ApplicationContextUtils.getSession().setAttribute("employee", employee);

//        return true;
        if (null != allowUrls && allowUrls.length >= 1)
            for (String url : allowUrls) {
                if (requestUrl.contains(url)) {
                    return true;
                }
            }

//        Employee employee = (Employee) request.getSession().getAttribute("employee");
//
//        if (employee != null) {
//            return true;
//        } else {
//            response.sendRedirect(request.getContextPath() + "/login.html");
//            return false;
//        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public String[] getAllowUrls() {
        return allowUrls;
    }

    public void setAllowUrls(String[] allowUrls) {
        this.allowUrls = allowUrls;
    }
}
