package com.dounine.japi.core.annotation;

import com.dounine.japi.core.type.RequestMethod;

import java.util.List;

/**
 * Created by lake on 17-2-7.
 */
public interface IActionRequest {

    boolean defaultValue();

    String valueField();

    String methodField();

    List<String[]> methodValues();

    RequestMethod getMethod();

    String annotation();

}
