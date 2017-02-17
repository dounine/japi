package com.dounine.japi.core.valid;

/**
 * Created by lake on 17-2-10.
 */
public class ValidAnnoVal {
    private String name;
    private String type;
    private String defaultVal;

    public ValidAnnoVal(String name,String type,String defaultVal){
        this.name = name;
        this.type = type;
        this.defaultVal = defaultVal;
    }

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

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }
}
