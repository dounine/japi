package com.dounine.japi.core.impl;

import com.dounine.japi.core.IParameterField;
import com.dounine.japi.core.IReturnField;
import com.dounine.japi.core.IReturnFieldDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class ReturnFieldImpl implements IReturnField {
    private List<String> annotations = new ArrayList<>();
    private String type;
    private List<IReturnFieldDoc> docs = new ArrayList<>();

    @Override
    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<IReturnFieldDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<IReturnFieldDoc> docs) {
        this.docs = docs;
    }
}
