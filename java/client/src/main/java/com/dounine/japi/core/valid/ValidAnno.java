package com.dounine.japi.core.valid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-10.
 */
public class ValidAnno {
    private String name;
    private List<ValidAnnoVal> validAnnoVals = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ValidAnnoVal> getValidAnnoVals() {
        return validAnnoVals;
    }

    public void setValidAnnoVals(List<ValidAnnoVal> validAnnoVals) {
        this.validAnnoVals = validAnnoVals;
    }
}
