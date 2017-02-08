package com.dounine.japi.core.annotation.impl;

import com.dounine.japi.core.annotation.IActionRequest;

/**
 * Created by lake on 17-2-7.
 */
public class ActionRequest implements IActionRequest {
    private boolean defaultValue;
    private String valueField;
    private String annotation;

    public ActionRequest() {

    }

    public ActionRequest(String annotation, boolean defaultValue, String valueField) {
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
}
