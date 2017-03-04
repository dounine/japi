package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.serial.response.IResponse;
import com.dounine.japi.serial.response.ResponseImpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-3-4.
 */
public class ActionReturnParser {

    public static List<IResponse> getResponses(Class<?> returnType,List<String[]> docs){
        List<IResponse> responses = new ArrayList<>();
        for(Method method : returnType.getMethods()) {
            ResponseImpl responseImpl = new ResponseImpl();
            responseImpl.setName(method.getName().substring(3).toLowerCase());
            responseImpl.setType(TypeConvert.getHtmlType(method.getReturnType().getSimpleName()));
            responses.add(responseImpl);
        }
        return responses;
    }
}
