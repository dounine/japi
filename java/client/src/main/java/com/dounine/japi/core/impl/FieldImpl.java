package com.dounine.japi.core.impl;

import com.dounine.japi.core.IField;
import com.dounine.japi.core.IFieldDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class FieldImpl implements IField {
    private List<String> annotations = new ArrayList<>();
    private List<IField> returnFields;
    private String name;
    private String type;
    private List<IFieldDoc> docs = new ArrayList<>();

    @Override
    public List<IField> getReturnFields() {
        return returnFields;
    }

    public void setReturnFields(List<IField> returnFields) {
        this.returnFields = returnFields;
    }

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
    public List<IFieldDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<IFieldDoc> docs) {
        this.docs = docs;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
