package com.dounine.japi.core;

import com.dounine.japi.core.annotation.impl.ActionRequest;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IActionMethod {

    List<IActionMethodDoc> getDocs();

    List<String> getAnnotations();

    String getMethodDescription();

    IType getType();

    ActionRequest getRequest();

    List<String> getParameters();

}
