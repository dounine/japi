package com.dounine.japi;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.act.ResultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by huanghuanlai on 2017/1/14.
 */
@Component
public class ActionExceptionHandler extends AbstractHandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionExceptionHandler.class);
    private static final String JSON_CONTEXT = "application/json;charset=utf-8";
    private static final int SUCCESS_STATUS = 200;
    private static final int EXCEPTION_STATUS = 500;
    private static final int EXCEPTION_CODE = 1;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ResultImpl actResult = new ResultImpl();
        httpServletResponse.setContentType(JSON_CONTEXT);
        if (e instanceof JapiException) {
            httpServletResponse.setStatus(SUCCESS_STATUS);
        } else {
            httpServletResponse.setStatus(EXCEPTION_STATUS);
            actResult.setCode(EXCEPTION_CODE);
            e.printStackTrace();
        }
        actResult.setMsg(e.getMessage());
        try {
            httpServletResponse.getWriter().print(JSON.toJSON(actResult));
        } catch (IOException e1) {
            e1.printStackTrace();
            return new ModelAndView();
        }
        return new ModelAndView();
    }
}
