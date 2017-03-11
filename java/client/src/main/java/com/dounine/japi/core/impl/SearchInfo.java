package com.dounine.japi.core.impl;

import com.dounine.japi.core.impl.types.ClassType;

import java.io.File;

/**
 * Created by lake on 17-3-9.
 */
public class SearchInfo {
    private File file;
    private String key = "";
    private ClassType classType;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }
}
