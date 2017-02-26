package com.dounine.japi.core.impl;

import com.dounine.japi.core.IParameter;
import com.dounine.japi.serial.request.IRequest;
import com.dounine.japi.serial.request.RequestImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-10.
 */
public class ParameterImpl implements IParameter {
    private List<IRequest> requestFields = new ArrayList<>();

    @Override
    public List<IRequest> getRequestFields() {
        return requestFields;
    }

    public void setRequestFields(List<IRequest> requestFields) {
        this.requestFields = requestFields;
    }
}
