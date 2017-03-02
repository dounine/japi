package com.dounine.japi.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public interface IConfig {
    /**
     * get master project java path
     * @return /home/lake/github/japi/java/client/src/main/java"
     */
    String getProjectJavaPath();

    /**
     * get master project java path
     * @return com/dounine/japi/action
     */
    String getActionReletivePath();

    /**
     * get other reference include project java path
     * @return {"/home/lake/github/japi/java/client1/src/main/java","/home/lake/github/japi/java/api/src/main/java"}
     */
    List<String> getIncludeProjectJavaPath();

    String[] getIncludePackages();
}
