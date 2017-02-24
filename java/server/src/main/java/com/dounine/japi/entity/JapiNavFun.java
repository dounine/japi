package com.dounine.japi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-24.
 */
public class JapiNavFun {

    private String name;
    private List<JapiNavAction> actions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JapiNavAction> getActions() {
        return actions;
    }

    public void setActions(List<JapiNavAction> actions) {
        this.actions = actions;
    }
}
