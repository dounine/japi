package com.dounine.japi.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-24.
 */
public class JapiNavAction {

    private String name;
    private List<JapiNavVersion> versions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JapiNavVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<JapiNavVersion> versions) {
        this.versions = versions;
    }
}
