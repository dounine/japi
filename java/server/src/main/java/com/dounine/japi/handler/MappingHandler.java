package com.dounine.japi.handler;

import com.dounine.japi.web.HtmlRender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ike on 17-1-16.
 */
@Component
public class MappingHandler implements HandlerInterceptor {

    @Value("${htmlRootPath}")
    private String htmlRootPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(null!=modelAndView&&!modelAndView.getViewName().equals("error")){
            System.out.println("modelAndView:"+modelAndView.getViewName());
            //HtmlRender.show(response,htmlRootPath+"/"+modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
