package com.dounine.japi;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.*;
import com.dounine.japi.core.impl.ConfigImpl;
import com.dounine.japi.core.impl.DocTagImpl;
import com.dounine.japi.core.impl.ProjectImpl;
import com.dounine.japi.core.impl.TypeImpl;
import com.dounine.japi.core.impl.response.ActionInfo;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-23.
 */
public class JapiClient {

    private static final JapiClient JAPI_CLIENT = new JapiClient();

    private ConfigImpl config = new ConfigImpl();
    private JapiClient(){}

    public static final IConfig getConfig(){
        return JAPI_CLIENT.config;
    }

    public static void main(String[] args) {
        JapiClient.setProjectJavaPath("/home/lake/github/japi/java/client/src/main/java");
        JapiClient.setActionReletivePath("com/dounine/japi/core/action");
        JapiClient.setIncludeProjectJavaPath(new String[]{"/home/lake/github/japi/java/api/src/main/java"});

        IProject project = ProjectImpl.init();

        for(IPackage iPackage : project.getPackages()){
            List<IAction> actions = iPackage.getActions();
            for(IAction action : actions){
                List<IActionMethod> actionMethods = action.getMethods();
                List<ActionInfo> actionInfos = action.getActionInfos(actionMethods);
                System.out.println(JSON.toJSONString(actionInfos,true));
            }
        }
    }

    public static void setProjectJavaPath(String projectJavaPath) {
        JAPI_CLIENT.config.setProjectJavaPath(projectJavaPath);
    }

    public static void setActionReletivePath(String actionReletivePath) {
        JAPI_CLIENT.config.setActionReletivePath(actionReletivePath);
    }

    public  static void setIncludeProjectJavaPath(List<String> includeProjectJavaPath) {
        JAPI_CLIENT.config.setIncludeProjectJavaPath(includeProjectJavaPath);
    }

    public static void setIncludeProjectJavaPath(String[] includeProjectJavaPath) {
        if(null!=includeProjectJavaPath){
            JAPI_CLIENT.config.setIncludeProjectJavaPath(Arrays.asList(includeProjectJavaPath));
        }
    }
}
