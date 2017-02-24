package com.dounine.japi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-24.
 */
public class JapiNavRoot {
    private List<JapiNavPackage> packages = new ArrayList<>();

    public List<JapiNavPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<JapiNavPackage> packages) {
        this.packages = packages;
    }
}
