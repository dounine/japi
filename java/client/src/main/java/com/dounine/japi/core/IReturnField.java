package com.dounine.japi.core;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IReturnField {

    List<String> getAnnotations();

    List<IReturnField> getReturnFields();

    String getName();

    String getType();

    List<IReturnFieldDoc> getDocs();
}
