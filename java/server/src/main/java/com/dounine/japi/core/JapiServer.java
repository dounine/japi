package com.dounine.japi.core;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.auth.UserAuth;
import com.dounine.japi.auth.UserUtils;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.transfer.*;
import com.dounine.japi.web.type.FollowEnum;
import com.dounine.japi.web.TransferInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-24.
 */
public class JapiServer {
    private static final Integer VERSION = 8;

    private static String projectsPath = null;
    private static String usersPath = null;
    private final static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
    private static final Pattern JAPI_TAG_NAME = Pattern.compile("japi[.][a-zA-Z0-9_]*\\s*[=]\\s*");

    static {
        projectsPath = FileUtils.getUserDirectoryPath() + "/.japi-server/projects/";
        usersPath = FileUtils.getUserDirectoryPath() + "/.japi-server/users/";
        File japiProjectsDir = new File(projectsPath);
        File japiUsersDir = new File(usersPath);
        if (!japiProjectsDir.exists()) {
            japiProjectsDir.mkdir();
        }
        if (!japiUsersDir.exists()) {
            japiUsersDir.mkdir();
        }
    }

    private static final FileFilter FILE_FILTER = DirectoryFileFilter.DIRECTORY;

    public List<JapiProject> getAllProjects() {
        List<JapiProject> projects = new ArrayList<>();
        File serverFold = new File(projectsPath);
        for (File file : serverFold.listFiles(FILE_FILTER)) {
            if (file.isHidden()) {
                continue;
            }
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
            if (!logoFile.exists()) {
                logoFile = new File(file.getAbsolutePath() + "/logo.jpg");
            }
            if (!logoFile.exists()) {
                logoFile = new File(file.getAbsolutePath() + "/logo.gif");
            }
            if (logoFile.exists()) {
                japiProject.setIcon(true);
            }
            projects.add(japiProject);
        }
        return projects;
    }

    public JapiNavRoot getProjectNav(String projectName) {
        JapiNavRoot japiNavRoot = new JapiNavRoot();
        if (StringUtils.isNotBlank(projectName)) {
            File projectFold = new File(projectsPath + "/" + projectName);
            if (!projectFold.exists()) {
                throw new JapiException(projectName + " project not exists.");
            }
            List<JapiNavPackage> japiNavPackages = new ArrayList<>();
            for (File file : projectFold.listFiles(FILE_FILTER)) {
                if (file.isHidden()) {
                    continue;
                }
                JapiNavPackage japiNavPackage = new JapiNavPackage();
                japiNavPackage.setName(file.getName());
                File packageFold = new File(projectsPath + "/" + projectName + "/" + file.getName());
                if (!packageFold.exists()) {
                    throw new JapiException(file.getName() + " package not exists.");
                }
                for (File packageFile : packageFold.listFiles(FILE_FILTER)) {
                    if (packageFile.isHidden()) {
                        continue;
                    }
                    JapiNavFun japiNavFun = new JapiNavFun();
                    japiNavFun.setName(packageFile.getName());
                    File funFold = new File(projectsPath + "/" + projectName + "/" + file.getName() + "/" + packageFile.getName());
                    if (!funFold.exists()) {
                        throw new JapiException(packageFile.getName() + " fun not exists.");
                    }
                    for (File actionFile : funFold.listFiles(FILE_FILTER)) {
                        if (actionFile.isHidden()) {
                            continue;
                        }
                        JapiNavAction japiNavAction = new JapiNavAction();
                        japiNavAction.setName(actionFile.getName());
                        japiNavAction.setVersions(null);
                        japiNavFun.getActions().add(japiNavAction);
                    }
                    japiNavPackage.getFuns().add(japiNavFun);
                }
                japiNavPackages.add(japiNavPackage);
            }
            japiNavRoot.setPackages(japiNavPackages);
            return japiNavRoot;
        }
        throw new JapiException("projectName not empty.");
    }

    public List<String> getActionVersions(TransferInfo transferInfo) {
        File actionFile = new File(projectsPath + "/" + transferInfo.getProjectName() + "/" + transferInfo.getPackageName() + "/" + transferInfo.getFunName() + "/" + transferInfo.getActionName());
        if(!actionFile.exists()){
            throw new JapiException(transferInfo.getActionName()+" 没有此方法");
        }
        List<String> versions = new ArrayList<>();
        for (File vFile : actionFile.listFiles(FILE_FILTER)) {
            if (vFile.isHidden()) {
                continue;
            }
            versions.add(vFile.getName());
        }
        return versions;
    }

    public List<String> getActionVerDates(TransferInfo transferInfo) {
        File actionFile = new File(projectsPath + "/" + transferInfo.getProjectName() + "/" + transferInfo.getPackageName() + "/" + transferInfo.getFunName() + "/" + transferInfo.getActionName() + "/" + transferInfo.getVersionName());
        if(!actionFile.exists()){
            throw new JapiException(transferInfo.getVersionName()+" 没有此版本.");
        }
        List<String> versions = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (File vFile : actionFile.listFiles(FILE_FILTER)) {
            if (vFile.isHidden()) {
                continue;
            }
            calendar.setTimeInMillis(Long.parseLong(vFile.getName()));
            versions.add(formatter.format(calendar.getTime()));
        }
        return versions;
    }

    public InputStream getIconInputStream(String projectName) {
        File iconFile = new File(projectsPath + "/" + projectName + "/logo.png");
        if (!iconFile.exists()) {
            iconFile = new File(projectsPath + "/" + projectName + "/logo.jpg");
        }
        if (!iconFile.exists()) {
            iconFile = new File(projectsPath + "/" + projectName + "/logo.gif");
        }
        if (iconFile.exists()) {
            try {
                return new FileInputStream(iconFile.getAbsoluteFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return new FileInputStream(JapiServer.class.getResource("/logo.png").getFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getAction(TransferInfo transferInfo) {
        try {
            String millDate = "" + formatter.parse(transferInfo.getDateName()).getTime();
            File actionFile = new File(projectsPath + "/" + transferInfo.getProjectName() + "/" + transferInfo.getPackageName() + "/" + transferInfo.getFunName() + "/" + transferInfo.getActionName() + "/" + transferInfo.getVersionName() + "/" + millDate + "/info.txt");
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

    private void checkUUID(String projectName,String uuid){
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName not empty.");
        }
        if(StringUtils.isBlank(uuid)){
            throw new JapiException("服务器:"+projectName+"项目uuid不能为空,请检查.");
        }
        File uuidFile = new File(projectsPath+"/"+projectName+"/uuid.txt");
        String _uuid = null;
        if(uuidFile.exists()){
            try {
                _uuid = FileUtils.readFileToString(uuidFile,Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            throw new JapiException("服务器:"+projectName+"项目uuid文件不存在,请检查.");
        }


        if(StringUtils.isBlank(_uuid)){
            throw new JapiException("服务器:"+projectName+"项目uuid读取错误,请检查.");
        }
        if(!_uuid.trim().equals(uuid)){
            throw new JapiException("服务器:"+projectName+"项目uuid不相同,请检查.");
        }
    }

    public void saveProjectInfo(TransferInfo transferInfo, String fileName, InputStream is) {
        checkUUID(transferInfo.getProjectName(),transferInfo.getUuid());
        File file = new File(projectsPath + "/" + transferInfo.getProjectName() + "/" + fileName);
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createProjectFold(TransferInfo transferInfo) {
        File fold = new File(projectsPath + "/" + transferInfo.getProjectName());
        if (!fold.exists()) {
            File uuidFile = new File(projectsPath+"/"+transferInfo.getProjectName()+"/uuid.txt");
            fold.mkdir();
            try {
                FileUtils.writeStringToFile(uuidFile,transferInfo.getUuid(),"utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            File uuidFile = new File(projectsPath+"/"+transferInfo.getProjectName()+"/uuid.txt");
            String uuid = null;
            try {
                uuid = FileUtils.readFileToString(uuidFile,"utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(StringUtils.isBlank(uuid)){
                throw new JapiException("服务器:"+transferInfo.getProjectName()+"项目uuid读取错误,请检查.");
            }
            if(!uuid.equals(transferInfo.getUuid())){
                throw new JapiException("服务器:"+transferInfo.getProjectName()+"项目uuid不相同,请检查.");
            }
        }
    }


    public String getLogoMd5(TransferInfo transferInfo) {
        checkUUID(transferInfo.getProjectName(),transferInfo.getUuid());
        File file = new File(projectsPath + "/" + transferInfo.getProjectName() + "/logo-md5.txt");
        if (file.exists()) {
            try {
                return FileUtils.readFileToString(file, Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getProjectMd5(TransferInfo transferInfo) {
        checkUUID(transferInfo.getProjectName(),transferInfo.getUuid());
        File file = new File(projectsPath + "/" + transferInfo.getProjectName() + "/project-md5.txt");
        if (file.exists()) {
            try {
                return FileUtils.readFileToString(file, Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getActionMd5(TransferInfo transferInfo) {
        checkUUID(transferInfo.getProjectName(),transferInfo.getUuid());
        if (null == transferInfo) {
            throw new JapiException("projectName packageName funName actionName versionName dateName not empty.");
        }
        if (StringUtils.isBlank(transferInfo.getPackageName())) {
            throw new JapiException("packageName not empty.");
        }
        if (StringUtils.isBlank(transferInfo.getFunName())) {
            throw new JapiException("funName not empty.");
        }
        if (StringUtils.isBlank(transferInfo.getActionName())) {
            throw new JapiException("actionName not empty.");
        }
        if (StringUtils.isBlank(transferInfo.getVersionName())) {
            throw new JapiException("versionName not empty.");
        }
        if (StringUtils.isBlank(transferInfo.getDateName())) {
            throw new JapiException("dateName not empty.");
        }
        File file = new File(projectsPath + "/" + transferInfo.getProjectName() + "/" + transferInfo.getPackageName() + "/" + transferInfo.getFunName() + "/" + transferInfo.getActionName() + "/" + transferInfo.getVersionName() + "/" + transferInfo.getDateName() + "/md5.txt");
        if (file.exists()) {
            try {
                return FileUtils.readFileToString(file, Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void createNavs(TransferInfo transferInfo, JapiNavRoot japiNavRoot) {
        if (null == transferInfo) {
            throw new JapiException("projectName not empty.");
        }
        if (StringUtils.isBlank(transferInfo.getUuid())) {
            throw new JapiException("application uuid not empty.");
        }
        if (StringUtils.isBlank(transferInfo.getProjectName())) {
            throw new JapiException("projectName not empty.");
        }
        String projectPath = projectsPath + "/" + transferInfo.getProjectName();
        for (JapiNavPackage japiNavPackage : japiNavRoot.getPackages()) {
            File packageFile = new File(projectPath + "/" + japiNavPackage.getName());
            if (!packageFile.exists()) {
                packageFile.mkdir();
            }
            for (JapiNavFun japiNavFun : japiNavPackage.getFuns()) {
                File funFile = new File(packageFile.getAbsoluteFile() + "/" + japiNavFun.getName());
                if (!funFile.exists()) {
                    funFile.mkdir();
                }
                for (JapiNavAction japiNavAction : japiNavFun.getActions()) {
                    File actionFile = new File(funFile.getAbsoluteFile() + "/" + japiNavAction.getName());
                    if (!actionFile.exists()) {
                        actionFile.mkdir();
                    }
                    for (JapiNavVersion japiNavVersion : japiNavAction.getVersions()) {
                        File versionFile = new File(actionFile.getAbsoluteFile() + "/" + japiNavVersion.getName());
                        if (!versionFile.exists()) {
                            versionFile.mkdir();
                        }
                        for (JapiNavDate japiNavDate : japiNavVersion.getDates()) {
                            File dataFile = new File(versionFile.getAbsoluteFile() + "/" + japiNavDate.getName());
                            if (!dataFile.exists()) {
                                dataFile.mkdir();
                            }
                        }
                    }

                }
            }
        }
    }

    public void saveActionInfo(TransferInfo transferInfo, String originalFilename, InputStream is) {
        checkUUID(transferInfo.getProjectName(),transferInfo.getUuid());
        File file = new File(projectsPath + "/" + transferInfo.getProjectName() + "/" + transferInfo.getPackageName() + "/" + transferInfo.getFunName() + "/" + transferInfo.getActionName() + "/" + transferInfo.getVersionName() + "/" + transferInfo.getDateName() + "/" + originalFilename);
        try {
            if (!file.getParentFile().exists()) {
                throw new JapiException(file.getParent() + " fold not exists.");
            }
            FileUtils.copyInputStreamToFile(is, file);
            String md5Str = DigestUtils.md5Hex(FileUtils.readFileToString(file, Charset.forName("utf-8")));
            FileUtils.writeStringToFile(new File(file.getParentFile() + "/md5.txt"), md5Str, Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> sortAndDel(String token,String[] projects) {
        UserAuth userAuth = UserUtils.getUserAuth(token);
        File userFollowFile = new File(usersPath + userAuth.getUsername() + "/follow.txt");
        try {
            List<String> projectsLists = Arrays.asList(projects);
            FileUtils.writeStringToFile(userFollowFile, JSON.toJSONString(projectsLists), Charset.forName("utf-8"));
            return projectsLists;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<String> getFollows(String token) {
        UserAuth userAuth = UserUtils.getUserAuth(token);
        File userFollowFile = new File(usersPath + userAuth.getUsername() + "/follow.txt");
        if(userFollowFile.exists()){
            try {
                return JSON.parseArray(FileUtils.readFileToString(userFollowFile, Charset.forName("utf-8")), String.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public void follow(String token, String projectName, FollowEnum add) {
        UserAuth userAuth = UserUtils.getUserAuth(token);
        File userFold = new File(usersPath + userAuth.getUsername());
        if (!userFold.exists()) {
            userFold.mkdir();
        }
        File userFollowFile = new File(userFold.getAbsolutePath() + "/follow.txt");
        if (!userFollowFile.exists()) {
            if (FollowEnum.ADD.equals(add)) {
                try {
                    List<String> projects = new ArrayList<>();
                    projects.add(projectName);
                    FileUtils.writeStringToFile(userFollowFile, JSON.toJSONString(projects), Charset.forName("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (FollowEnum.ADD.equals(add)) {
                try {
                    List<String> projects = JSON.parseArray(FileUtils.readFileToString(userFollowFile, Charset.forName("utf-8")), String.class);
                    if(!projects.contains(projectName)){
                        projects.add(projectName);
                    }
                    FileUtils.writeStringToFile(userFollowFile, JSON.toJSONString(projects), Charset.forName("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    List<String> projects = JSON.parseArray(FileUtils.readFileToString(userFollowFile, Charset.forName("utf-8")), String.class);
                    if(projects.contains(projectName)){
                        Iterator<String> projectsIterator = projects.iterator();
                        while (projectsIterator.hasNext()) {
                            if (projectsIterator.next().equals(projectName)) {
                                projectsIterator.remove();
                                break;
                            }
                        }
                    }
                    FileUtils.writeStringToFile(userFollowFile, JSON.toJSONString(projects), Charset.forName("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkClientVersion(Integer clientVersion) throws JapiException{
        if(null==clientVersion){
            throw new JapiException("您的JAPI客户端过低,请更新升级后再使用.");
        }
        if(clientVersion<VERSION){
            throw new JapiException("您的JAPI客户端过低,请更新升级后再使用.");
        }
    }

    public void flush(TransferInfo transferInfo) {
        File projectFold = new File(projectsPath+transferInfo.getProjectName());
        if(projectFold.exists()){
            FileUtils.deleteQuietly(projectFold);
        }
    }
}
