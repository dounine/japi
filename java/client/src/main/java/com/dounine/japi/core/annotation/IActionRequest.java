package com.dounine.japi.core.annotation;

import com.dounine.japi.serial.type.RequestMethod;

import java.util.List;

/**
 * Created by lake on 17-2-7.
 */
public interface IActionRequest {

    String valueField();

    String methodField();

    List<String[]> methodValues();

    RequestMethod getMethod();

    String annotation();

}
