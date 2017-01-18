package com.dounine.japi.core.impl;

import com.dounine.japi.core.IAction;
import com.dounine.japi.core.IMethod;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class ActionImpl implements IAction {
    @Override
    public String readClassInfo() {
        return null;
    }

    @Override
    public String readPackageName() {
        return null;
    }

    @Override
    public IMethod[] readMethod() {
        return new IMethod[0];
    }
}
