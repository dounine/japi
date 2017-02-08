package com.dounine.japi.core.annotation;

import com.dounine.japi.core.type.RequestMethod;

/**
 * Created by lake on 17-2-7.
 */
public interface IActionRequest {

    boolean defaultValue();

    String valueField();

    RequestMethod getMethod();

    String annotation();

}
