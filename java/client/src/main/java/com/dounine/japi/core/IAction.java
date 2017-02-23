package com.dounine.japi.core;

import com.dounine.japi.core.impl.response.ActionInfo;

import javax.swing.*;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IAction {

    List<IActionMethod> getMethods();

    List<ActionInfo> getActionInfos(List<IActionMethod> actionMethods);

    String getName();
}
