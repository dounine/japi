package com.dounine.japi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-24.
 */
public class JapiNavPackage {

    private String name;
    private List<JapiNavFun> funs = new ArrayList<>();

    public List<JapiNavFun> getFuns() {
        return funs;
    }

    public void setFuns(List<JapiNavFun> funs) {
        this.funs = funs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
