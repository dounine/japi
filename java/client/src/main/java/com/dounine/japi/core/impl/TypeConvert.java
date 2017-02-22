package com.dounine.japi.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lake on 17-2-22.
 */
public final class TypeConvert {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeConvert.class);

    private TypeConvert(){}
    private static final Map<String,String> TYPES = new HashMap<>();
    static {
        TYPES.put("Integer","number");
        TYPES.put("String","string");
        TYPES.put("Double","number");
        TYPES.put("double","number");
        TYPES.put("int","number");
        TYPES.put("Float","number");
        TYPES.put("float","number");
        TYPES.put("Object","object");
        TYPES.put("Boolean","boolean");
        TYPES.put("boolean","boolean");
        TYPES.put("long","number");
        TYPES.put("Long","number");
        TYPES.put("LocalDateTime","string");
        TYPES.put("LocalDate","string");
        TYPES.put("LocalTime","string");
    }

    public static final String getHtmlType(String type){
        String htmlType = TYPES.get(type);
        if(null==htmlType){
            return "object";
        }
        return htmlType;
    }
}
