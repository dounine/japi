package com.dounine.japi.core.annotation.impl;

import com.dounine.japi.core.type.RequestMethod;

/**
 * Created by lake on 17-2-8.
 */
public class ActionRequest {
    private String[] urls;
    private RequestMethod[] methods;
    public ActionRequest(){}
    public ActionRequest(String[] urls,RequestMethod[] method){
        this.urls = urls;
        this.methods = method;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public RequestMethod[] getMethods() {
        return methods;
    }

    public void setMethods(RequestMethod[] methods) {
        this.methods = methods;
    }
}
