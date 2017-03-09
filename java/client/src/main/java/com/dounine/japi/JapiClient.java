package com.dounine.japi;

import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.core.IConfig;
import com.dounine.japi.core.IProject;
import com.dounine.japi.core.impl.ConfigImpl;
import com.dounine.japi.core.impl.ProjectImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        JapiClient.setPrefixPath("/home/lake/github/japi/java/");//路径前缀
        JapiClient.setpostfixPath("/src/main/java");

        JapiClient.setProjectJavaPath("client");
        JapiClient.setActionReletivePath("com/dounine/japi/core/action");
        JapiClient.setIncludeProjectJavaPath(new String[]{"api"});
        JapiClient.setIncludePackages(new String[]{"com.dounine.japi"});//可以准确快速搜索
        JapiClient.setUseCache(true);//

        IProject project = ProjectImpl.init();
        JapiClientStorage japiClientStorage = JapiClientStorage.getInstance();
        japiClientStorage.setProject(project);
        japiClientStorage.autoSaveToDisk();
        new JapiClientTransfer().autoTransfer(japiClientStorage);
    }

    /**
     * 设置主项目地扯
     * @param projectJavaPath
     */
    public static void setProjectJavaPath(String projectJavaPath) {
        JAPI_CLIENT.config.setProjectJavaPath(projectJavaPath);
    }

    /**
     * pringmvc action reletivePath
     * @param actionReletivePath
     */
    public static void setActionReletivePath(String actionReletivePath) {
        JAPI_CLIENT.config.setActionReletivePath(actionReletivePath);
    }

    public  static void setIncludeProjectJavaPath(List<String> includeProjectJavaPath) {
        JAPI_CLIENT.config.setIncludeProjectJavaPath(includeProjectJavaPath);
    }

    public static void setIncludePackages(String[] includePackages) {
        JAPI_CLIENT.config.setIncludePackages(includePackages);
    }

    public static void setPrefixPath(String prefixPath){
        JAPI_CLIENT.config.setPrefixPath(prefixPath);
    }

    public static void setpostfixPath(String postfixPath){
        JAPI_CLIENT.config.setPostfixPath(postfixPath);
    }


    public static void setIncludeProjectJavaPath(String[] includeProjectJavaPath) {
        if(null!=includeProjectJavaPath){
            JAPI_CLIENT.config.setIncludeProjectJavaPath(Arrays.asList(includeProjectJavaPath));
        }
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void setUseCache(boolean useCache) {
        JAPI_CLIENT.useCache = useCache;
    }
    public static boolean isUseCache(){
        return JAPI_CLIENT.useCache;
    }
}
