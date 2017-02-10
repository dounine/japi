package com.dounine.japi.core.impl;

import com.dounine.japi.core.IParameter;

/**
 * Created by lake on 17-2-10.
 */
public class ParameterImpl implements IParameter {
    private String type;
    private String name;
    private String anno;

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }
}
