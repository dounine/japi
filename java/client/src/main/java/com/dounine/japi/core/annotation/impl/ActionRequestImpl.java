package com.dounine.japi.core.annotation.impl;

import com.dounine.japi.core.annotation.IActionRequest;
import com.dounine.japi.core.type.RequestMethod;

/**
 * Created by lake on 17-2-7.
 */
public class ActionRequestImpl implements IActionRequest {
    private boolean defaultValue;
    private String valueField;
    private String annotation;
    private RequestMethod method = RequestMethod.GET;

    public ActionRequestImpl() {

    }

    public ActionRequestImpl(RequestMethod method, String annotation, boolean defaultValue, String valueField) {
        this.method = method;
        this.annotation = annotation;
        this.defaultValue = defaultValue;
        this.valueField = valueField;
    }

    @Override
    public boolean defaultValue() {
        return defaultValue;
    }

    @Override
    public String valueField() {
        return valueField;
    }

    @Override
    public String annotation() {
        return annotation;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }
}
