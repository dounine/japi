package com.dounine.japi.core.impl;

import com.dounine.japi.core.IField;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.serial.request.IRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class FieldImpl implements IField {
    private List<String> annotations = new ArrayList<>();
    private List<IField> fields;
    private boolean enumType;
    private String name;
    private String type;
    private List<IFieldDoc> docs = new ArrayList<>();
    private IRequest request;

    @Override
    public List<IField> getFields() {
        return fields;
    }

    public void setFields(List<IField> fields) {
        this.fields = fields;
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

    public boolean isEnumType() {
        return enumType;
    }

    @Override
    public IRequest enumRequest() {
        return request;
    }

    public void setRequest(IRequest request) {
        this.request = request;
    }

    public void setEnumType(boolean enumType) {
        this.enumType = enumType;
    }
}
