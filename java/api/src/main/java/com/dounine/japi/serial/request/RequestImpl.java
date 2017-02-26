package com.dounine.japi.serial.request;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/2/24.
 */
public class RequestImpl implements IRequest{
    private String name;
    private String type;
    private List<IRequest> fields;
    private boolean required = false;
    private String defaultValue;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<IRequest> getFields() {
        return fields;
    }

    @Override
    public boolean getRequired() {
        return required;
    }

    public void setFields(List<IRequest> fields) {
        this.fields = fields;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
