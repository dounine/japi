package com.dounine.japi.config;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.auth.UserUtils;
import com.dounine.japi.exception.JapiException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huanghuanlai on 2017/2/27.
 */
@Component
public class ErrorRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(200!=httpServletResponse.getStatus()){
            ResultImpl result = new ResultImpl();
            httpServletResponse.setHeader("Content-Type","application/json;charset=UTF-8");
            if(httpServletResponse.getStatus()==404){
                result.setMsg("访问的请求不存在.");
            }else if(httpServletResponse.getStatus()==400){
                result.setMsg("请检查请求参数.");
            }else if(httpServletResponse.getStatus()==500){
                result.setMsg(((Exception)o).getMessage());
            }else if(httpServletResponse.getStatus()==405){
                result.setMsg("请求方式不支持,请检查.");
            }
            result.setCode(httpServletResponse.getStatus());
            httpServletResponse.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
