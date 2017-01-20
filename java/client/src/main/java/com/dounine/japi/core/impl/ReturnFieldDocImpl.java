package com.dounine.japi.core.impl;

import com.dounine.japi.core.IReturnFieldDoc;

/**
 * Created by huanghuanlai on 2017/1/20.
 */
public class ReturnFieldDocImpl implements IReturnFieldDoc {

    private String name;
    private String value;
    private String des;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
