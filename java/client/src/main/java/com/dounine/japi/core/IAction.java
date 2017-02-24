package com.dounine.japi.core;


import com.dounine.japi.serial.ActionInfo;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IAction {

    List<IActionMethod> getMethods();

    List<ActionInfo> getActionInfos(List<IActionMethod> actionMethods);

    String getName();
}
