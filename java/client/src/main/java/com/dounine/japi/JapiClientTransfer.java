package com.dounine.japi;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.transfer.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lake on 17-2-24.
 */
public class JapiClientTransfer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JapiClientTransfer.class);
    private static final int reties = 3;
    private static final FileFilter FILE_FILTER = DirectoryFileFilter.DIRECTORY;
    private static String token = null;
    /**
     * seconds
     */
    private static final int tryTime = 10;
    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();

    private Result postValues(String url, String[] datas) {
        List<String[]> da = new ArrayList<>(1);
        da.add(datas);
        return postValues(url, datas);
    }

    private Result postValues(String url, List<String[]> datas) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> valuePairs = new ArrayList<>();
        for (String[] keyValue : datas) {
            valuePairs.add(new BasicNameValuePair(keyValue[0], keyValue[1]));
        }
        Integer tryCount = 0;
        while (reties > tryCount) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "utf-8"));
                httpPost.setHeader("token", token);
                String result = EntityUtils.toString(HTTP_CLIENT.execute(httpPost).getEntity(), Consts.UTF_8);
                Result result1 = JSON.parseObject(result, ResultImpl.class);
                if (result1.getCode() != 0) {
                    throw new JapiException(result1.getMsg());
                }
                return result1;
            } catch (IOException e) {
                if (e instanceof HttpHostConnectException) {
                    tryCount++;
                    LOGGER.warn("try connect server " + tryCount + " count.");
                    try {
                        TimeUnit.SECONDS.sleep(tryTime);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        if (tryCount <= reties) {
            LOGGER.error("server connect failed.");
        }
        return null;
    }

    private Result postFile(String url, List<String[]> datas, File file) {
        if (!file.exists()) {
            throw new JapiException(file.getAbsolutePath() + " file not exist.");
        }
        HttpPost httpPost = new HttpPost(url);
        Integer tryCount = 0;
        while (reties > tryCount) {
            try {
                final MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                multipartEntity.setCharset(Charset.forName("utf-8"));
                multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                for (String[] nameValue : datas) {
                    multipartEntity.addPart(nameValue[0], new StringBody(nameValue[1], ContentType.APPLICATION_JSON));
                }
                multipartEntity.addBinaryBody("file", file);
                httpPost.setEntity(multipartEntity.build());
                httpPost.setHeader("token", token);
                String result = EntityUtils.toString(HTTP_CLIENT.execute(httpPost).getEntity());
                Result result1 = JSON.parseObject(result, ResultImpl.class);
                if (result1.getCode() != 0) {
                    throw new JapiException(result1.getMsg());
                }
                return result1;
            } catch (IOException e) {
                if (e instanceof HttpHostConnectException) {
                    tryCount++;
                    LOGGER.warn("try connect server " + tryCount + " count.");
                    try {
                        TimeUnit.SECONDS.sleep(tryTime);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        if (tryCount <= reties) {
            LOGGER.error("server connect failed.");
        }
        return null;
    }

    private File getLogoFile(JapiClientStorage japiClientStorage) {
        String prePath = japiClientStorage.getJapiPath() + japiClientStorage.getProject().getProperties().get("japi.name");
        File logoFile = new File(prePath + "/logo.png");
        if (!logoFile.exists()) {
            logoFile = new File(prePath + "/logo.jpg");
        }
        if (!logoFile.exists()) {
            logoFile = new File(prePath + "/logo.gif");
        }
        return logoFile;
    }

    private File getLogoMd5File(JapiClientStorage japiClientStorage) {
        String prePath = japiClientStorage.getJapiPath() + japiClientStorage.getProject().getProperties().get("japi.name");
        return new File(prePath + "/logo-md5.txt");
    }

    public void transferProjectLogo(JapiClientStorage japiClientStorage) {
        List<String[]> datas = new ArrayList<>();
        String projectName = japiClientStorage.getProject().getProperties().get("japi.name");
        datas.add(new String[]{"projectName", projectName});
        datas.add(new String[]{"uuid", japiClientStorage.getProject().getProperties().get("japi.uuid")});
        String serverUrl = japiClientStorage.getProject().getProperties().get("japi.server");
        File logoFile = getLogoFile(japiClientStorage);
        if (logoFile.exists()) {
            datas.add(new String[]{"type", "logo"});
            Result md5Result = postValues(serverUrl + "/transfer/project/md5", datas);
            if (md5Result.getData() == null) {
                postFile(serverUrl + "/transfer/project/info", datas, logoFile);
                postFile(serverUrl + "/transfer/project/info", datas, getLogoMd5File(japiClientStorage));
            } else {
                try {
                    String logoFileMd5Str = FileUtils.readFileToString(getLogoMd5File(japiClientStorage), Charset.forName("utf-8"));
                    if (!logoFileMd5Str.equals(md5Result.getData().toString())) {
                        postFile(serverUrl + "/transfer/project/info", datas, logoFile);
                        postFile(serverUrl + "/transfer/project/info", datas, getLogoMd5File(japiClientStorage));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void transferProjectInfo(JapiClientStorage japiClientStorage) {
        String prePath = japiClientStorage.getJapiPath() + japiClientStorage.getProject().getProperties().get("japi.name");
        List<String[]> datas = new ArrayList<>();
        String projectName = japiClientStorage.getProject().getProperties().get("japi.name");
        datas.add(new String[]{"projectName", projectName});
        datas.add(new String[]{"uuid", japiClientStorage.getProject().getProperties().get("japi.uuid")});
        String serverUrl = getServerPath(japiClientStorage);
        File projectFile = new File(prePath + "/project-info.txt");
        File projectMd5File = new File(prePath + "/project-md5.txt");
        if (projectFile.exists()) {
            datas.add(new String[]{"type", "project"});
            Result md5Result = postValues(serverUrl + "/transfer/project/md5", datas);
            if (md5Result.getData() == null) {
                postFile(serverUrl + "/transfer/project/info", datas, projectFile);
                postFile(serverUrl + "/transfer/project/info", datas, projectMd5File);
            } else {
                try {
                    String logoFileMd5Str = FileUtils.readFileToString(projectMd5File, Charset.forName("utf-8"));
                    if (!logoFileMd5Str.equals(md5Result.getData().toString())) {
                        postFile(serverUrl + "/transfer/project/info", datas, projectFile);
                        postFile(serverUrl + "/transfer/project/info", datas, projectMd5File);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            postFile(serverUrl + "/transfer/project/info", datas, projectFile);
            postFile(serverUrl + "/transfer/project/info", datas, projectMd5File);
        }
    }

    private String getServerPath(JapiClientStorage japiClientStorage) {
        return japiClientStorage.getProject().getProperties().get("japi.server");
    }

    private boolean autoLogin(JapiClientStorage japiClientStorage) {
        String serverPath = getServerPath(japiClientStorage);
        List<String[]> datas = new ArrayList<>();
        datas.add(new String[]{"username", japiClientStorage.getProject().getProperties().get("japi.server.username")});
        datas.add(new String[]{"password", DigestUtils.md5Hex(japiClientStorage.getProject().getProperties().get("japi.server.password"))});
        Result login = postValues(serverPath + "/user/login", datas);
        if (login.getCode() != 0 || login.getData() == null) {
            throw new JapiException(login.getMsg());
        } else {
            token = login.getData().toString();
            return true;
        }
    }


    public void autoTransfer(JapiClientStorage japiClientStorage) {
        String applicationUUID = japiClientStorage.getProject().getProperties().get("japi.uuid");
        if (StringUtils.isBlank(applicationUUID)) {
            throw new JapiException("应用程度UUID不能为空");
        }
        if (!autoLogin(japiClientStorage)) {
            return;
        }
        String serverUrl = getServerPath(japiClientStorage);
        String projectName = japiClientStorage.getProject().getProperties().get("japi.name");
        List<String[]> datas = new ArrayList<>();
        datas.add(new String[]{"projectName", projectName});
        datas.add(new String[]{"uuid", japiClientStorage.getProject().getProperties().get("japi.uuid")});
        datas.add(new String[]{"clientVersion", JapiClient.CLIENT_VERSION+""});
        if(JapiClient.isFlushServer()){
            LOGGER.info("强制清空服务器历史版本中...");
            postValues(serverUrl + "/transfer/project/flush", datas);
            LOGGER.info("强制清空服务器历史版本完成");
        }
        Result result = postValues(serverUrl + "/transfer/project/exists", datas);
        if (!result.getData().equals(Boolean.TRUE)) {//project exist
            postValues(serverUrl + "/transfer/project", datas);
        }


        transferProjectLogo(japiClientStorage);//project logo
        transferProjectInfo(japiClientStorage);//project info

        JapiNavRoot japiNavRoot = new JapiNavRoot();
        String projectPath = japiClientStorage.getJapiPath() + projectName;
        File projectFile = new File(projectPath);

        for (File packageFile : projectFile.listFiles(FILE_FILTER)) {
            if (packageFile.isHidden()) {
                continue;
            }
            JapiNavPackage japiNavPackage = new JapiNavPackage();
            japiNavPackage.setName(packageFile.getName());
            for (File funFile : packageFile.listFiles(FILE_FILTER)) {
                if (funFile.isHidden()) {
                    continue;
                }
                JapiNavFun japiNavFun = new JapiNavFun();
                japiNavFun.setName(funFile.getName());
                for (File actionFile : funFile.listFiles(FILE_FILTER)) {
                    if (actionFile.isHidden()) {
                        continue;
                    }
                    JapiNavAction japiNavAction = new JapiNavAction();
                    japiNavAction.setName(actionFile.getName());
                    for (File versionFile : actionFile.listFiles(FILE_FILTER)) {
                        if (versionFile.isHidden() && !versionFile.getName().startsWith("v\\d+")) {
                            continue;
                        }
                        JapiNavVersion japiNavVersion = new JapiNavVersion();
                        japiNavVersion.setName(versionFile.getName());
                        for (File dateFile : versionFile.listFiles(FILE_FILTER)) {
                            if (dateFile.isHidden() && !versionFile.getName().matches("\\d+")) {
                                continue;
                            }
                            JapiNavDate japiNavDate = new JapiNavDate();
                            japiNavDate.setName(dateFile.getName());
                            japiNavVersion.getDates().add(japiNavDate);
                        }
                        japiNavAction.getVersions().add(japiNavVersion);
                    }
                    japiNavFun.getActions().add(japiNavAction);
                }
                japiNavPackage.getFuns().add(japiNavFun);
            }
            japiNavRoot.getPackages().add(japiNavPackage);
        }
        if (japiNavRoot.getPackages().size() > 0) {
            datas.add(new String[]{"data", JSON.toJSONString(japiNavRoot)});
            datas.add(new String[]{"uuid", japiClientStorage.getProject().getProperties().get("japi.uuid")});
            postValues(serverUrl + "/transfer/navs", datas);
            for (JapiNavPackage japiNavPackage : japiNavRoot.getPackages()) {
                for (JapiNavFun japiNavFun : japiNavPackage.getFuns()) {
                    for (JapiNavAction japiNavAction : japiNavFun.getActions()) {
                        for (JapiNavVersion japiNavVersion : japiNavAction.getVersions()) {
                            for (JapiNavDate japiNavDate : japiNavVersion.getDates()) {
                                List<String[]> das = new ArrayList<>();
                                das.add(new String[]{"projectName", projectName});
                                das.add(new String[]{"packageName", japiNavPackage.getName()});
                                das.add(new String[]{"funName", japiNavFun.getName()});
                                das.add(new String[]{"actionName", japiNavAction.getName()});
                                das.add(new String[]{"versionName", japiNavVersion.getName()});
                                das.add(new String[]{"dateName", japiNavDate.getName()});
                                das.add(new String[]{"type", "action"});
                                das.add(new String[]{"uuid", japiClientStorage.getProject().getProperties().get("japi.uuid")});
                                Result md5Result = postValues(serverUrl + "/transfer/project/md5", das);
                                if (md5Result.getData() == null) {
                                    File infoFile = new File(projectPath + "/" + japiNavPackage.getName() + "/" + japiNavFun.getName() + "/" + japiNavAction.getName() + "/" + japiNavVersion.getName() + "/" + japiNavDate.getName() + "/info.txt");
                                    postFile(serverUrl + "/transfer/action/info", das, infoFile);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
