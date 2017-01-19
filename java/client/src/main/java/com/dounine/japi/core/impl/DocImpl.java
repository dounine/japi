package com.dounine.japi.core.impl;

import com.dounine.japi.core.IDoc;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public class DocImpl implements IDoc {
    private String name;
    private String value;

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
}
