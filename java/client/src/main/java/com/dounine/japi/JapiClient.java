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
    public static final Integer CLIENT_VERSION = 14;
    private static final JapiClient JAPI_CLIENT = new JapiClient();

    private ConfigImpl config = new ConfigImpl();
    private boolean saveHistory = false;
    private boolean flushServer = false;
    private Class classLoader = null;
    private boolean del = false;

    private JapiClient(){}

    public static final IConfig getConfig(){
        return JAPI_CLIENT.config;
    }

    public static void main(String[] args) {
        JapiClient.setClassLoader(JapiClient.class);//用于读取外部配置文件
        JapiClient.setPrefixPath("/home/lake/github/japi/java/");//路径前缀
        JapiClient.setpostfixPath("/src/main/java");

        JapiClient.setProjectJavaPath("client");
        JapiClient.setActionReletivePath("com/dounine/japi/core/action");
        JapiClient.setIncludeProjectJavaPath(new String[]{"api"});
        JapiClient.setIncludePackages(new String[]{"com.dounine.japi"});//可以准确快速搜索
        JapiClient.saveHistory(true);//保留本地历史版本
        JapiClient.setFlushServer(false);//强制同步本地与服务器所有版本
        //JapiClient.delete(true);//删除服务器上的项目

        IProject project = ProjectImpl.init();
        JapiClientStorage japiClientStorage = JapiClientStorage.getInstance();
        japiClientStorage.setProject(project);
        japiClientStorage.autoSaveToDisk();

        new JapiClientTransfer().autoTransfer(japiClientStorage);
    }

    public static void delete(boolean del) {
        JAPI_CLIENT.del = del;
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

    public static void saveHistory(boolean save) {
        JAPI_CLIENT.saveHistory = save;
    }
    public static boolean isSaveHistory(){
        return JAPI_CLIENT.saveHistory;
    }

    public static boolean isFlushServer(){
        return JAPI_CLIENT.flushServer;
    }

    public static void setFlushServer(boolean flushServer) {
        JAPI_CLIENT.flushServer = flushServer;
    }

    public static boolean isDel(){
        return JAPI_CLIENT.del;
    }

    public static Class getClassLoader() {
        return JAPI_CLIENT.classLoader;
    }

    public static void setClassLoader(Class classLoader) {
        JAPI_CLIENT.classLoader = classLoader;
    }
}
