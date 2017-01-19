package com.dounine.japi.core.impl;

import com.dounine.japi.core.IDoc;
import com.dounine.japi.core.IMethod;

import java.lang.annotation.Annotation;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class MethodImpl implements IMethod{

    private String[] annotations;
    private String[] parameters;
    private String returnType;

    private IDoc[] docs;

    public IDoc[] getDocs() {
        return docs;
    }

    public void setDocs(IDoc[] docs) {
        this.docs = docs;
    }

    @Override
    public String[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String[] annotations) {
        this.annotations = annotations;
    }

    @Override
    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
