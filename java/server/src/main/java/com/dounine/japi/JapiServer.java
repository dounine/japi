package com.dounine.japi;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.entity.*;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-24.
 */
public class JapiServer {
    private static String serverPath = null;

    static {
        serverPath = FileUtils.getUserDirectoryPath() + "/.japi-server/";
        File japiClientDir = new File(serverPath);
        if (!japiClientDir.exists()) {
            japiClientDir.mkdir();
        }
    }

    private static final Pattern JAPI_TAG_NAME = Pattern.compile("japi[.][a-zA-Z0-9_]*\\s*[=]\\s*");

    public static List<JapiProject> getAllProjects() {
        List<JapiProject> projects = new ArrayList<>();
        File serverFold = new File(serverPath);
        for (File file : serverFold.listFiles()) {
            JapiProject japiProject = new JapiProject();
            japiProject.setName(file.getName());
            File fileInfo = new File(file.getAbsolutePath() + "/project-info.txt");
            if (fileInfo.exists()) {
                try {
                    List<String> strLines = FileUtils.readLines(fileInfo, Charset.forName("utf-8"));
                    for (String line : strLines) {
                        Matcher matcher = JAPI_TAG_NAME.matcher(line);
                        if (matcher.find()) {
                            String name = StringUtils.substring(matcher.group(), 5, -1);
                            String value = line.substring(matcher.end()).trim();
                            if (name.equals("createTime")) {
                                japiProject.setCreateTime(value);
                            } else if (name.equals("author")) {
                                japiProject.setAuthor(value);
                            } else if (name.equals("version")) {
                                japiProject.setVersion(value);
                            } else if (name.equals("description")) {
                                japiProject.setDescription(value);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File logoFile = new File(file.getAbsolutePath() + "/logo.png");
            if(!logoFile.exists()){
                logoFile = new File(file.getAbsolutePath() + "/logo.jpg");
            }
            if(!logoFile.exists()){
                logoFile = new File(file.getAbsolutePath() + "/logo.gif");
            }
            if(logoFile.exists()){
                japiProject.setIcon(true);
            }
            projects.add(japiProject);
        }
        return projects;
    }

    public static JapiNavRoot getProjectNav(String projectName) {
        JapiNavRoot japiNavRoot = new JapiNavRoot();
        if (StringUtils.isNotBlank(projectName)) {
            File projectFold = new File(serverPath + "/" + projectName);
            if (!projectFold.exists()) {
                throw new JapiException(projectName + " project not exists.");
            }
            List<JapiNavPackage> japiNavPackages = new ArrayList<>();
            for (File file : projectFold.listFiles()) {
                if (file.isDirectory()) {
                    JapiNavPackage japiNavPackage = new JapiNavPackage();
                    japiNavPackage.setName(file.getName());
                    File packageFold = new File(serverPath + "/" + projectName + "/" + file.getName());
                    if (!packageFold.exists()) {
                        throw new JapiException(file.getName() + " package not exists.");
                    }
                    for (File packageFile : packageFold.listFiles()) {
                        JapiNavFun japiNavFun = new JapiNavFun();
                        japiNavFun.setName(packageFile.getName());
                        File funFold = new File(serverPath + "/" + projectName + "/" + file.getName() + "/" + packageFile.getName());
                        if (!funFold.exists()) {
                            throw new JapiException(packageFile.getName() + " fun not exists.");
                        }
                        for (File actionFile : funFold.listFiles()) {
                            JapiNavAction japiNavAction = new JapiNavAction();
                            japiNavAction.setName(actionFile.getName());
                            japiNavFun.getActions().add(japiNavAction);
                        }
                        japiNavPackage.getFuns().add(japiNavFun);
                    }
                    japiNavPackages.add(japiNavPackage);
                }
            }
            japiNavRoot.setPackages(japiNavPackages);
            return japiNavRoot;
        }
        throw new JapiException("projectName not empty.");
    }

    public static List<String> getActionVersions(String projectName, String packageName, String funName, String actionName) {
        File actionFile = new File(serverPath + "/" + projectName + "/" + packageName + "/" + funName + "/" + actionName);
        List<String> versions = new ArrayList<>();
        for (File vFile : actionFile.listFiles()) {
            versions.add(vFile.getName());
        }
        return versions;
    }


    static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");

    public static List<String> getActionVerDates(String projectName, String packageName, String funName, String actionName, String version) {
        File actionFile = new File(serverPath + "/" + projectName + "/" + packageName + "/" + funName + "/" + actionName + "/" + version + "/date");
        List<String> versions = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (File vFile : actionFile.listFiles()) {
            calendar.setTimeInMillis(Long.parseLong(vFile.getName()));
            versions.add(formatter.format(calendar.getTime()));
        }
        return versions;
    }

    public static InputStream getIconInputStream(String projectName) {
        File iconFile = new File(serverPath + "/" + projectName + "/logo.png");
        if(!iconFile.exists()){
            iconFile = new File(serverPath + "/" + projectName + "/logo.jpg");
        }
        if(!iconFile.exists()){
            iconFile = new File(serverPath + "/" + projectName + "/logo.gif");
        }
        if(iconFile.exists()){
            try {
                return new FileInputStream(iconFile.getAbsoluteFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getAction(String projectName, String packageName, String funName, String actionName, String version, String date) {
        try {
            String millDate = "" + formatter.parse(date).getTime();
            File actionFile = new File(serverPath + "/" + projectName + "/" + packageName + "/" + funName + "/" + actionName + "/" + version + "/date/" + millDate + "/info.txt");
            if (actionFile.exists()) {
                return FileUtils.readFileToString(actionFile, Charset.forName("utf-8"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static void main(String[] args) {
//        System.out.println(JSON.toJSONString(getAllProjects()));
//        System.out.println(JSON.toJSONString(getProjectNav("test")));
//        System.out.println(JSON.toJSON(getActionVersions("test", "测试类集合", "测试类", "测试例子")));
//        System.out.println(JSON.toJSON(getActionVerDates("test", "测试类集合", "测试类", "测试例子", "v1")));
//        System.out.println(getAction("test", "测试类集合", "测试类", "测试例子", "v1", "2017-02-24 18:25:14:22"));
//    }


}
