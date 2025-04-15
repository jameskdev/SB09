package org.xm.sb09.interceptors;

import java.lang.reflect.Method;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecondInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("Second interceptor afterCompletion");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        log.info("Second interceptor postHandle");
        if (modelAndView != null) {
            ModelAndView mv = (ModelAndView) modelAndView.getModelMap().get("modelAndView");
            String q = (String) mv.getModel().get("query");

            if (q != null) {
                modelAndView.addObject("query", q.toUpperCase());
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod hm = (HandlerMethod) handler;
        Method method = hm.getMethod();
        log.info("Second interceptor preHandle");
        log.info("Method is: " + method.getName());
        return true;
    }
}
