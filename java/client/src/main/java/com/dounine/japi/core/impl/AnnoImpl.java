package com.dounine.japi.core.impl;

import com.dounine.japi.core.IAnno;
import com.dounine.japi.core.IAnnoVal;

import java.util.List;

/**
 * Created by lake on 17-2-10.
 */
public class AnnoImpl implements IAnno {
    private String name;
    private List<IAnnoVal> annoVals;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<IAnnoVal> getAnnoVals() {
        return annoVals;
    }

    public void setAnnoVals(List<IAnnoVal> annoVals) {
        this.annoVals = annoVals;
    }
}
