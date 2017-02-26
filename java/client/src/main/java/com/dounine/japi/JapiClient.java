package com.dounine.japi;

import com.dounine.japi.core.IConfig;
import com.dounine.japi.core.IProject;
import com.dounine.japi.core.impl.ConfigImpl;
import com.dounine.japi.core.impl.ProjectImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lake on 17-2-23.
 */
public class JapiClient {

    private static final JapiClient JAPI_CLIENT = new JapiClient();

    private ConfigImpl config = new ConfigImpl();
    private boolean useCache = false;
    private JapiClient(){}

    public static final IConfig getConfig(){
        return JAPI_CLIENT.config;
    }

    public static void main(String[] args) {
        JapiClient.setProjectJavaPath("/home/lake/github/japi/java/client/src/main/java");
        JapiClient.setActionReletivePath("com/dounine/japi/core/action");
        JapiClient.setIncludeProjectJavaPath(new String[]{"/home/lake/github/japi/java/api/src/main/java"});
        JapiClient.setUseCache(false);//

        IProject project = ProjectImpl.init();
        JapiClientStorage japiClientStorage = JapiClientStorage.getInstance();
        japiClientStorage.setProject(project);
        japiClientStorage.autoSaveToDisk();
        new JapiClientTransfer().autoTransfer(japiClientStorage);
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


    public static void setUseCache(boolean useCache) {
        JAPI_CLIENT.useCache = useCache;
    }
    public static boolean isUseCache(){
        return JAPI_CLIENT.useCache;
    }
}
