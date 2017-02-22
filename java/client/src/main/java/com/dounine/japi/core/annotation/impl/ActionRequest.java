package com.dounine.japi.core.annotation.impl;

import com.dounine.japi.core.type.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-8.
 */
public class ActionRequest {
    private List<String> urls = new ArrayList<>();
    private RequestMethod[] methods;
    public ActionRequest(){}
    public ActionRequest(List<String> urls,RequestMethod[] method){
        this.urls = urls;
        this.methods = method;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public RequestMethod[] getMethods() {
        return methods;
    }

    public void setMethods(RequestMethod[] methods) {
        this.methods = methods;
    }
}
