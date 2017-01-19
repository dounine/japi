package com.dounine.japi.core;

import java.lang.annotation.Annotation;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IMethod {

    IDoc[] getDocs();

    String[] getAnnotations();

    String getReturnType();

    String[] getParameters();

}
