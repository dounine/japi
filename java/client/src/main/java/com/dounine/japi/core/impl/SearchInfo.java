package com.dounine.japi.core.impl;

import java.io.File;

/**
 * Created by lake on 17-3-9.
 */
public class SearchInfo {
    private File file;
    private String key = "";

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
}
