package com.dounine.japi.core.impl.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public class ActionInfoRequest {
    private List<String> urls = new ArrayList<>();
    private List<String> methods = new ArrayList<>();

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
