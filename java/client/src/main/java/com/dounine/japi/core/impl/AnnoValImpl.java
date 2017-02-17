package com.dounine.japi.core.impl;

import com.dounine.japi.core.IAnnoVal;

/**
 * Created by lake on 17-2-10.
 */
public class AnnoValImpl implements IAnnoVal {
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
