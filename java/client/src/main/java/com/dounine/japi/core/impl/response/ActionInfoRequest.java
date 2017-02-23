package com.dounine.japi.core.impl.response;

import com.dounine.japi.core.type.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public class ActionInfoRequest {
    private List<String> urls = new ArrayList<>();
    private List<RequestMethod> methods = new ArrayList<>();

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<RequestMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<RequestMethod> methods) {
        this.methods = methods;
    }
}
