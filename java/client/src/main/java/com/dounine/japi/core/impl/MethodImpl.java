package com.dounine.japi.core.impl;

import com.dounine.japi.core.IActionMethod;
import com.dounine.japi.core.IActionMethodDoc;
import com.dounine.japi.core.IType;
import com.dounine.japi.core.annotation.impl.ActionRequest;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class MethodImpl implements IActionMethod {

    private List<String> annotations;
    private List<String> parameters;
    private IType type;
    private String methodDescription;
    private ActionRequest request;

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

    @Override
    public String getMethodDescription() {
        return methodDescription;
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

    public ActionRequest getRequest() {
        return request;
    }

    public void setRequest(ActionRequest request) {
        this.request = request;
    }


    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    @Override
    public IType getType() {
        return type;
    }

    public void setType(IType type) {
        this.type = type;
    }
}
