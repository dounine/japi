package com.dounine.japi.core;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IAction {

    String readClassInfo();

    String readPackageName();

    List<String> getExcludeTypes();

    List<IMethod> getMethods();

}
