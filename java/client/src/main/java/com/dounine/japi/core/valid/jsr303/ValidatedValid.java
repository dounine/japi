package com.dounine.japi.core.valid.jsr303;

import com.dounine.japi.core.valid.IMVC;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-17.
 */
public class ValidatedValid implements IMVC{

    private String projectPath;
    private String javaFilePath;
    private List<String> includePaths = new ArrayList<>();

    public ValidatedValid(String projectPath,String javaFilePath,List<String> includePaths){
        this.projectPath = projectPath;
        this.javaFilePath = javaFilePath;
        this.includePaths = includePaths;
    }

    @Override
    public String getRequestParamName() {
        return "org.springframework.validation.annotation.Validated";
    }

    @Override
    public String getRequestInfo(String annoStr, String typeStr, String nameStr,List<String> docsStrs) {
        return null;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }
}
