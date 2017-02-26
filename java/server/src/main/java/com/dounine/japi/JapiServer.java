package com.dounine.japi;

import com.dounine.japi.exception.JapiException;
import com.dounine.japi.transfer.*;
import com.dounine.japi.web.TransferInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private final static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
    private static final Pattern JAPI_TAG_NAME = Pattern.compile("japi[.][a-zA-Z0-9_]*\\s*[=]\\s*");

    static {
        serverPath = FileUtils.getUserDirectoryPath() + "/.japi-server/";
        File japiClientDir = new File(serverPath);
        if (!japiClientDir.exists()) {
            japiClientDir.mkdir();
        }
    }

    public List<JapiProject> getAllProjects() {
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

    public List<String> getActionVersions(String projectName, String packageName, String funName, String actionName) {
        File actionFile = new File(serverPath + "/" + projectName + "/" + packageName + "/" + funName + "/" + actionName);
        List<String> versions = new ArrayList<>();
        for (File vFile : actionFile.listFiles()) {
            versions.add(vFile.getName());
        }
        return versions;
    }

    public List<String> getActionVerDates(String projectName, String packageName, String funName, String actionName, String version) {
        File actionFile = new File(serverPath + "/" + projectName + "/" + packageName + "/" + funName + "/" + actionName + "/" + version + "/date");
        List<String> versions = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (File vFile : actionFile.listFiles()) {
            calendar.setTimeInMillis(Long.parseLong(vFile.getName()));
            versions.add(formatter.format(calendar.getTime()));
        }
        return versions;
    }

    public InputStream getIconInputStream(String projectName) {
        File iconFile = new File(serverPath + "/" + projectName + "/logo.png");
        if (!iconFile.exists()) {
            iconFile = new File(serverPath + "/" + projectName + "/logo.jpg");
        }
        if (!iconFile.exists()) {
            iconFile = new File(serverPath + "/" + projectName + "/logo.gif");
        }
        if (iconFile.exists()) {
            try {
                return new FileInputStream(iconFile.getAbsoluteFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getAction(String projectName, String packageName, String funName, String actionName, String version, String date) {
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

    public void saveProjectInfo(String projectName, String fileName, InputStream is) {
        File file = new File(serverPath + "/" + projectName + "/" + fileName);
        try {
            FileUtils.copyInputStreamToFile(is,file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createProjectFold(String projectName) {
        File file = new File(serverPath + "/" + projectName);
        if (!file.exists()) {
            file.mkdir();
        }
    }


    public String getLogoMd5(String projectName) {
        File file = new File(serverPath + "/" + projectName + "/logo-md5.txt");
        if (file.exists()) {
            try {
                return FileUtils.readFileToString(file, Charset.forName("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getProjectMd5(String projectName) {
        File file = new File(serverPath + "/" + projectName + "/project-md5.txt");
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
        File file = new File(serverPath + "/" + transferInfo.getProjectName() + "/" + transferInfo.getPackageName() + "/" + transferInfo.getFunName() + "/" + transferInfo.getActionName() + "/" + transferInfo.getVersionName() + "/" + transferInfo.getDateName() + "/md5.txt");
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
        createProjectFold(transferInfo.getProjectName());
        String projectPath = serverPath + "/" + transferInfo.getProjectName();
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
        File file = new File(serverPath + "/" + transferInfo.getProjectName() + "/" + transferInfo.getPackageName() + "/" + transferInfo.getFunName() + "/" + transferInfo.getActionName() + "/" + transferInfo.getVersionName() + "/" + transferInfo.getDateName() + "/"+originalFilename);
        try {
            if(!file.getParentFile().exists()){
                throw new JapiException(file.getParent()+" fold not exists.");
            }
            FileUtils.copyInputStreamToFile(is,file);
            String md5Str = DigestUtils.md5Hex(FileUtils.readFileToString(file,Charset.forName("utf-8")));
            FileUtils.writeStringToFile(new File(file.getParentFile()+"/md5.txt"),md5Str,Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
