package com.dounine.japi.entity;

/**
 * 测试类型
 * Created by lake on 17-3-11.
 */
public enum TestType {

    /**
     * 增加
     */
    ADD("0"),

    /**
     * 删除
     */
    DEL("1");

    private String code;

    TestType(String code){
        this.code = code;
    }

}
