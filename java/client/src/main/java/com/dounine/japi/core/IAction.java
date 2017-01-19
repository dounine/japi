package com.dounine.japi.core;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IAction {

    String readClassInfo();

    String readPackageName();

    IMethod[] getMethods();

}
