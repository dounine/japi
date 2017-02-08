package com.dounine.japi.core.annotation.impl;

import com.dounine.japi.core.type.RequestMethod;

/**
 * Created by lake on 17-2-8.
 */
public class ActionRequest {
    private String[] urls;
    private RequestMethod method = RequestMethod.GET;
    public ActionRequest(){}
    public ActionRequest(String[] urls,RequestMethod method){
        this.urls = urls;
        this.method = method;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }
}
