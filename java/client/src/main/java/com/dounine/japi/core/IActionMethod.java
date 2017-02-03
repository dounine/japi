package com.dounine.japi.core;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IActionMethod {

    List<IActionMethodDoc> getDocs();

    List<String> getAnnotations();

    IReturnType getReturnType();

    List<String> getParameters();

}
