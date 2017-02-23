package com.dounine.japi.core;

import java.util.List;
import java.util.Map;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public interface IProject {

    List<IPackage> getPackages();

    Map<String,String> getProperties();

}
