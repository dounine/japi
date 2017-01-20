package com.dounine.japi.core;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public interface IBuiltIn {

    List<String> getBuiltInTypes();

    boolean isBuiltInType(String keyType);

}
