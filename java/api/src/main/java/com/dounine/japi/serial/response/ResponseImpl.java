package com.dounine.japi.serial.response;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/2/24.
 */
public class ResponseImpl implements IResponse {
    private String name;
    private String type;
    private String description;
    private String defaultValue;
    private List<IResponse> fields;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public List<IResponse> getFields() {
        return fields;
    }

    public void setFields(List<IResponse> fields) {
        this.fields = fields;
    }
}
