package com.dounine.japi;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.core.IAction;
import com.dounine.japi.core.IActionMethod;
import com.dounine.japi.core.IPackage;
import com.dounine.japi.core.IProject;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.serial.ActionInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lake on 17-2-24.
 */
public class JapiClientStorage {
    private String japiPath = null;
    private IProject project;
    private static final String[] TIPS = new String[]{" name not empty.", " name forbid '/' symbol."};
    private static final Logger LOGGER = LoggerFactory.getLogger(JapiClientStorage.class);
    private static final JapiClientStorage JAPI_CLIENT_STORAGE = new JapiClientStorage();

    private JapiClientStorage(){}

    public static final JapiClientStorage getInstance(){
        return JAPI_CLIENT_STORAGE;
    }

    static {
        JAPI_CLIENT_STORAGE.japiPath = FileUtils.getUserDirectoryPath() + "/.japi-client/";
        File japiClientDir = new File(JAPI_CLIENT_STORAGE.japiPath);
        if (!japiClientDir.exists()) {
            japiClientDir.mkdir();
        }
    }

    public void createProjectDir(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("project" + TIPS[0]);
        }
        if (projectName.indexOf("/") != -1||projectName.indexOf(",") != -1) {
            throw new JapiException("project" + TIPS[1]);
        }
        File file = new File(JAPI_CLIENT_STORAGE.japiPath + projectName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void createPackageDir(String projectName, String packageName) {
        createProjectDir(projectName);
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("package" + TIPS[0]);
        }
        if (packageName.indexOf("/") != -1||packageName.indexOf(",") != -1) {
            throw new JapiException("package" + TIPS[1]);
        }
        File file = new File(JAPI_CLIENT_STORAGE.japiPath + projectName + "/" + packageName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void createFunDir(String projectName, String packageName, String funName) {
        createPackageDir(projectName, packageName);
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("fun" + TIPS[0]);
        }
        if (funName.indexOf("/") != -1||funName.indexOf(",") != -1) {
            throw new JapiException("fun" + TIPS[1]);
        }
        File file = new File(JAPI_CLIENT_STORAGE.japiPath + projectName + "/" + packageName + "/" + funName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void createActionDir(String projectName, String packageName, String funName, String actionName) {
        createFunDir(projectName, packageName, funName);
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("action" + TIPS[0]);
        }
        if (actionName.indexOf("/") != -1||actionName.indexOf(",") != -1) {
            throw new JapiException("action" + TIPS[1]);
        }
        File file = new File(JAPI_CLIENT_STORAGE.japiPath + projectName + "/" + packageName + "/" + funName + "/" + actionName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void createVersionDir(String projectName, String packageName, String funName, String actionName, String version) {
        createActionDir(projectName, packageName, funName, actionName);
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("version" + TIPS[0]);
        }
        if (actionName.indexOf("/") != -1||actionName.indexOf(",") != -1) {
            throw new JapiException("version" + TIPS[1]);
        }
        File file = new File(JAPI_CLIENT_STORAGE.japiPath + projectName + "/" + packageName + "/" + funName + "/" + actionName + "/" + version);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private void saveProjectInfo() {
        String pa = JAPI_CLIENT_STORAGE.japiPath + project.getProperties().get("japi.name");
        File projectInfoFile = new File(pa + "/project-info.txt");
        File projectInfoMd5File = new File(pa + "/project-md5.txt");
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : project.getProperties().keySet()) {
            stringBuffer.append(key + "=" + project.getProperties().get(key) + "\n");
        }
        if (!projectInfoFile.exists()) {
            try {
                projectInfoFile.createNewFile();
                projectInfoMd5File.createNewFile();
                FileUtils.writeStringToFile(projectInfoFile, stringBuffer.toString(), Charset.forName("utf-8"), true);
                FileUtils.writeStringToFile(projectInfoMd5File, DigestUtils.md5Hex(stringBuffer.toString()), Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (!DigestUtils.md5Hex(stringBuffer.toString()).equals(FileUtils.readFileToString(projectInfoMd5File, Charset.forName("utf-8")))) {
                    projectInfoFile.delete();
                    projectInfoMd5File.delete();
                    projectInfoFile.createNewFile();
                    projectInfoMd5File.createNewFile();
                    FileUtils.writeStringToFile(projectInfoFile, stringBuffer.toString(), Charset.forName("utf-8"), true);
                    FileUtils.writeStringToFile(projectInfoMd5File, DigestUtils.md5Hex(stringBuffer.toString()), Charset.forName("utf-8"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveLogo() {
        String projectName = project.getProperties().get("japi.name");
        String projectPath = JAPI_CLIENT_STORAGE.japiPath + projectName;
        File userLogoFile = new File(project.getProperties().get("japi.icon"));
        File logoFile = new File(projectPath + "/logo.png");
        File logoMd5File = new File(projectPath + "/logo-md5.txt");
        if (!logoFile.exists()) {
            logoFile = new File(projectPath + "/logo.jpg");
        }
        if (!logoFile.exists()) {
            logoFile = new File(projectPath + "/logo.gif");
        }
        if (logoFile.exists() && !logoMd5File.exists()) {
            FileUtils.deleteQuietly(logoFile);
        } else if (!logoFile.exists() && logoMd5File.exists()) {
            FileUtils.deleteQuietly(logoMd5File);
        }
        if (logoFile.exists() && userLogoFile.exists()) {
            if (logoMd5File.exists()) {
                try {
                    String md5Str = FileUtils.readFileToString(logoMd5File, Charset.forName("utf-8"));
                    String userLogoFileMd5 = DigestUtils.md5Hex(new FileInputStream(userLogoFile));
                    if (!userLogoFileMd5.equals(md5Str)) {
                        FileUtils.copyFile(userLogoFile, new File(projectPath + "/logo." + FilenameUtils.getExtension(userLogoFile.getName())));
                        FileUtils.writeStringToFile(logoMd5File, userLogoFileMd5, Charset.forName("utf-8"));
                        LOGGER.info("[ " + projectName + " ] project update logo image.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (!logoFile.exists() && userLogoFile.exists()) {
            try {
                FileUtils.copyFile(userLogoFile, new File(projectPath + "/logo." + FilenameUtils.getExtension(userLogoFile.getName())));
                String userLogoFileMd5 = DigestUtils.md5Hex(new FileInputStream(userLogoFile));
                FileUtils.writeStringToFile(logoMd5File, userLogoFileMd5, Charset.forName("utf-8"));
                LOGGER.info("[ " + projectName + " ] project create logo image.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("[ " + projectName + " ] project icon path [ " + project.getProperties().get("japi.icon") + " ] file not exists.");
        }
    }

    public void autoSaveToDisk() {
        if (!JapiClient.isUseCache()) {
            File file = new File(JAPI_CLIENT_STORAGE.japiPath + project.getProperties().get("japi.name"));
            if (file.exists()) {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String projectName = project.getProperties().get("japi.name");
        createProjectDir(projectName);
        saveProjectInfo();
        saveLogo();
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

    public void saveByTime(String projectName, String packageName, String funName, ActionInfo actionInfo) {
        createVersionDir(projectName, packageName, funName, actionInfo.getActionName(), actionInfo.getVersion());
        File versionFold = new File(JAPI_CLIENT_STORAGE.japiPath + projectName + "/" + packageName + "/" + funName + "/" + actionInfo.getActionName() + "/" + actionInfo.getVersion());
        File newDateFold = null;
        if (!versionFold.exists() || (null != versionFold && versionFold.list().length == 0)) {
            versionFold.mkdir();
            newDateFold = new File(versionFold.getAbsolutePath() + "/" + System.currentTimeMillis());
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
            List<File> dateFolds = new ArrayList<>();
            for (File file : versionFold.listFiles()) {
                if(file.isDirectory()&&!file.isHidden()){
                    dateFolds.add(file);
                }
            }
            dateFolds.sort((b, a) -> a.getName().compareTo(b.getName()));
            File millFold = dateFolds.get(0);
            if (millFold.getName().matches("\\d{13}")) {
                try {
                    String oldMd5 = FileUtils.readFileToString(new File(millFold.getAbsolutePath() + "/md5.txt"), Charset.forName("utf-8"));
                    String newMd5 = DigestUtils.md5Hex(JSON.toJSONString(actionInfo));
                    if (!newMd5.equals(oldMd5)) {
                        LOGGER.info(packageName + "/" + funName + "/" + actionInfo.getActionName() + " has modified.");
                        newDateFold = new File(versionFold.getAbsolutePath() + "/" + System.currentTimeMillis());
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

    private Map<String, List<ActionInfo>> getActionVersions(List<ActionInfo> actionInfos) {
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

    public String getJapiPath() {
        return JAPI_CLIENT_STORAGE.japiPath;
    }

    public void setJapiPath(String japiPath) {
        JAPI_CLIENT_STORAGE.japiPath = japiPath;
    }

    public IProject getProject() {
        return project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }
}
