package com.dounine.japi.core.impl;

import com.dounine.japi.core.IActionMethodDoc;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public class ActionMethodDocImpl implements IActionMethodDoc {

    private String name = "";
    private String docType;
    private String value = "";
    private String des = "";

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

    @Override
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
