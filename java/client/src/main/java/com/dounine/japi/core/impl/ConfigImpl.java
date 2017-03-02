package com.dounine.japi.core.impl;

import com.dounine.japi.core.IConfig;

import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public class ConfigImpl implements IConfig {

    private String projectJavaPath;
    private String actionReletivePath;
    private String[] includePackages;
    private List<String> includeProjectJavaPath;

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
}
