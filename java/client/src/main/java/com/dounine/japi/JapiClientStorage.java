package com.dounine.japi;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.core.IAction;
import com.dounine.japi.core.IActionMethod;
import com.dounine.japi.core.IPackage;
import com.dounine.japi.core.IProject;
import com.dounine.japi.core.impl.response.ActionInfo;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.datetime.joda.LocalDateParser;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by lake on 17-2-24.
 */
public class JapiClientStorage {
    private static String japiPath = null;
    private static final String[] TIPS = new String[]{" name not empty.", " name forbid '/' symbol."};
    private static final Logger LOGGER = LoggerFactory.getLogger(JapiClientStorage.class);

    static {
        japiPath = FileUtils.getUserDirectoryPath() + "/.japi-client/";
        File japiClientDir = new File(japiPath);
        if (!japiClientDir.exists()) {
            japiClientDir.mkdir();
        }
    }

    public static void createProjectDir(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("project" + TIPS[0]);
        }
        if (projectName.indexOf("/") != -1) {
            throw new JapiException("project" + TIPS[1]);
        }
        File file = new File(japiPath + projectName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void createPackageDir(String projectName, String packageName) {
        createProjectDir(projectName);
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("package" + TIPS[0]);
        }
        if (packageName.indexOf("/") != -1) {
            throw new JapiException("package" + TIPS[1]);
        }
        File file = new File(japiPath + projectName + "/" + packageName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void createFunDir(String projectName, String packageName, String funName) {
        createPackageDir(projectName, packageName);
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("fun" + TIPS[0]);
        }
        if (funName.indexOf("/") != -1) {
            throw new JapiException("fun" + TIPS[1]);
        }
        File file = new File(japiPath + projectName + "/" + packageName + "/" + funName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void createActionDir(String projectName, String packageName, String funName, String actionName) {
        createFunDir(projectName, packageName, funName);
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("action" + TIPS[0]);
        }
        if (actionName.indexOf("/") != -1) {
            throw new JapiException("action" + TIPS[1]);
        }
        File file = new File(japiPath + projectName + "/" + packageName + "/" + funName + "/" + actionName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void createVersionDir(String projectName, String packageName, String funName, String actionName, String version) {
        createActionDir(projectName, packageName, funName, actionName);
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("version" + TIPS[0]);
        }
        if (actionName.indexOf("/") != -1) {
            throw new JapiException("version" + TIPS[1]);
        }
        File file = new File(japiPath + projectName + "/" + packageName + "/" + funName + "/" + actionName + "/" + version);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void autoSaveToDisk(IProject project) {
        String projectName = project.getProperties().get("japi.name");
        createProjectDir(projectName);
        for (IPackage iPackage : project.getPackages()) {
            List<IAction> actions = iPackage.getActions();
            String packageName = iPackage.getName();
            createPackageDir(projectName, packageName);
            for (IAction action : actions) {
                String funName = action.getName();
                createFunDir(projectName, packageName, funName);
                List<IActionMethod> actionMethods = action.getMethods();
                List<ActionInfo> actionInfos = action.getActionInfos(actionMethods);
                Map<String, List<ActionInfo>> actionInfoMap = getActionVersions(actionInfos);
                for (String actionName : actionInfoMap.keySet()) {
                    createActionDir(projectName, packageName, funName, actionName);
                    for (ActionInfo versionInfo : actionInfoMap.get(actionName)) {
                        saveByTime(projectName, packageName, funName, versionInfo);
                    }
                }
            }
        }
    }

    public static void saveByTime(String projectName, String packageName, String funName, ActionInfo actionInfo) {
        createVersionDir(projectName, packageName, funName, actionInfo.getActionName(), actionInfo.getVersion());
        File dateFold = new File(japiPath + projectName + "/" + packageName + "/" + funName + "/" + actionInfo.getActionName() + "/" + actionInfo.getVersion() + "/date");
        File newDateFold = null;
        if (!dateFold.exists() || (null != dateFold && dateFold.list().length == 0)) {
            dateFold.mkdir();
            newDateFold = new File(dateFold.getAbsolutePath() + "/" + System.currentTimeMillis());
            newDateFold.mkdir();
            File infoFile = new File(newDateFold.getAbsolutePath() + "/info.txt");
            File md5File = new File(newDateFold.getAbsolutePath() + "/md5.txt");
            try {
                infoFile.createNewFile();
                md5File.createNewFile();
                FileUtils.writeStringToFile(infoFile, JSON.toJSONString(actionInfo), Charset.forName("utf-8"));
                FileUtils.writeStringToFile(md5File, DigestUtils.md5Hex(JSON.toJSONString(actionInfo)), Charset.forName("utf-8"));
                LOGGER.info(packageName + "/" + funName + "/" + actionInfo.getActionName() + " first created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final IOFileFilter javaFileFilter = FileFilterUtils.asFileFilter(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() && pathname.getName().matches("\\d{13}") && !pathname.getName().equals("date");
                }
            });

            List<File> dateFolds = new ArrayList<>();

            for (File file : dateFold.listFiles()) {
                dateFolds.add(file);
            }
            dateFolds.sort((b, a) -> a.getName().compareTo(b.getName()));
            File millFold = dateFolds.get(0);
            if (millFold.getName().matches("\\d{13}")) {
                try {
                    String oldMd5 = FileUtils.readFileToString(new File(millFold.getAbsolutePath() + "/md5.txt"), Charset.forName("utf-8"));
                    String newMd5 = DigestUtils.md5Hex(JSON.toJSONString(actionInfo));
                    if (!newMd5.equals(oldMd5)) {
                        LOGGER.info(packageName + "/" + funName + "/" + actionInfo.getActionName() + " has modified.");
                        newDateFold = new File(dateFold.getAbsolutePath() + "/" + System.currentTimeMillis());
                        newDateFold.mkdir();
                        File infoFile = new File(newDateFold.getAbsolutePath() + "/info.txt");
                        File md5File = new File(newDateFold.getAbsolutePath() + "/md5.txt");
                        try {
                            infoFile.createNewFile();
                            md5File.createNewFile();
                            FileUtils.writeStringToFile(infoFile, JSON.toJSONString(actionInfo), Charset.forName("utf-8"));
                            FileUtils.writeStringToFile(md5File, DigestUtils.md5Hex(JSON.toJSONString(actionInfo)), Charset.forName("utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Map<String, List<ActionInfo>> getActionVersions(List<ActionInfo> actionInfos) {
        Map<String, List<ActionInfo>> actionInfoMap = new HashMap<>();
        for (ActionInfo actionInfo : actionInfos) {
            if (actionInfoMap.get(actionInfo.getActionName()) == null) {
                actionInfoMap.put(actionInfo.getActionName(), new ArrayList<>());
                actionInfoMap.get(actionInfo.getActionName()).add(actionInfo);
            } else {
                actionInfoMap.get(actionInfo.getActionName()).add(actionInfo);
            }
        }
        return actionInfoMap;
    }

    public static void main(String[] args) {
        System.out.println("1234".matches("\\d{4}"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        System.out.println(now + " = " + formatter.format(calendar.getTime()));
//        createActionDir("test-project", "package1", "fun1", "action1");
    }
}
