package com.dounine.japi.core;

import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IPackage {

    List<IAction> getActions();

    String getName();
}
