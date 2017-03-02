package com.dounine.japi.config;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.auth.UserUtils;
import com.dounine.japi.exception.JapiException;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huanghuanlai on 2017/2/27.
 */
@Component
public class LoginRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Object tokenObject = httpServletRequest.getHeader("token");
        if(null==tokenObject){
            tokenObject = httpServletRequest.getParameter("token");
        }
        if(null==tokenObject){
            Cookie[] cookies = httpServletRequest.getCookies();
            if(null!=cookies){
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("token")){
                        tokenObject = cookie.getValue();
                        break;
                    }
                }
            }

        }
        if(null==tokenObject){
            throw new JapiException("请求头token不能为空");
        }
        String token = tokenObject.toString();
        if(!UserUtils.isAuth(token)){
            ResultImpl<String> result = new ResultImpl<>();
            result.setCode(1);
            result.setMsg("token无效");
            result.setData("/user/login");
            httpServletResponse.setHeader("Content-Type","application/json;charset=UTF-8");
            httpServletResponse.getWriter().print(JSON.toJSON(result));
            return false;
        }else {
            UserUtils.updateLiveTime(token);
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
