package com.dounine.japi.core.impl;

import com.dounine.japi.core.IConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public class ConfigImpl implements IConfig {

    private String projectJavaPath;
    private String actionReletivePath;
    private String[] includePackages;
    private List<String> includeProjectJavaPath = new ArrayList<>();
    private Class<?> actionDefaultReturnType;
    private String prefixPath = "";
    private String postfixPath = "";

    @Override
    public String getProjectJavaPath() {
        return projectJavaPath;
    }

    public void setProjectJavaPath(String projectJavaPath) {
        this.projectJavaPath = projectJavaPath;
    }

    @Override
    public List<String> getIncludeProjectJavaPath() {
        return includeProjectJavaPath;
    }

    public void setIncludeProjectJavaPath(List<String> includeProjectJavaPath) {
        this.includeProjectJavaPath = includeProjectJavaPath;
    }

    public String getActionReletivePath() {
        return actionReletivePath;
    }

    public void setActionReletivePath(String actionReletivePath) {
        this.actionReletivePath = actionReletivePath;
    }

    public String[] getIncludePackages() {
        return includePackages;
    }

    public void setIncludePackages(String[] includePackages) {
        this.includePackages = includePackages;
    }

    public Class<?> getActionDefaultReturnType() {
        return actionDefaultReturnType;
    }

    public void setActionDefaultReturnType(Class<?> actionDefaultReturnType) {
        this.actionDefaultReturnType = actionDefaultReturnType;
    }

    public void setPrefixPath(String prefixPath) {
        this.prefixPath = prefixPath;
    }

    public String getPrefixPath() {
        return prefixPath;
    }

    public String getPostfixPath() {
        return postfixPath;
    }

    public void setPostfixPath(String postfixPath) {
        this.postfixPath = postfixPath;
    }


}
