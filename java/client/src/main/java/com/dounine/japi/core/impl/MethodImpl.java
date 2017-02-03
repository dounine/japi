package com.dounine.japi.core.impl;

import com.dounine.japi.core.IActionMethod;
import com.dounine.japi.core.IActionMethodDoc;
import com.dounine.japi.core.IReturnType;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class MethodImpl implements IActionMethod {

    private List<String> annotations;
    private List<String> parameters;
    private IReturnType returnType;

    private List<IActionMethodDoc> docs;

    public List<IActionMethodDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<IActionMethodDoc> docs) {
        this.docs = docs;
    }

    @Override
    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public IReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(IReturnType returnType) {
        this.returnType = returnType;
    }
}
