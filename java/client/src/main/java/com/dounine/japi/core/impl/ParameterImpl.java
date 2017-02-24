package com.dounine.japi.core.impl;

import com.dounine.japi.core.IParameter;
import com.dounine.japi.core.impl.request.RequestImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-10.
 */
public class ParameterImpl implements IParameter {
    private List<RequestImpl> requestFields = new ArrayList<>();

    @Override
    public List<RequestImpl> getRequestFields() {
        return requestFields;
    }

    public void setRequestFields(List<RequestImpl> requestFields) {
        this.requestFields = requestFields;
    }
}
