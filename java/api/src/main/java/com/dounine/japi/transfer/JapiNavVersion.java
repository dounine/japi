package com.dounine.japi.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/2/26.
 */
public class JapiNavVersion {
    private String name;
    private List<JapiNavDate> dates = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JapiNavDate> getDates() {
        return dates;
    }

    public void setDates(List<JapiNavDate> dates) {
        this.dates = dates;
    }
}
