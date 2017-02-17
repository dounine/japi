package com.dounine.japi.core.impl;

import com.dounine.japi.core.IAnno;
import com.dounine.japi.core.IParameter;
import com.dounine.japi.core.IType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-10.
 */
public class ParameterImpl implements IParameter {
    private List<String> requestInfos = new ArrayList<>();

    public void setRequestInfos(List<String> requestInfos) {
        this.requestInfos = requestInfos;
    }

    @Override
    public String getRequestInfo() {
        return StringUtils.join(requestInfos.toArray(),",");
    }
}
