package com.dounine.japi;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.transfer.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
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
    /**
     * seconds
     */
    private static final int tryTime = 3;
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
                String result = EntityUtils.toString(HTTP_CLIENT.execute(httpPost).getEntity(), Consts.UTF_8);
                return JSON.parseObject(result, ResultImpl.class);
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
                String result = EntityUtils.toString(HTTP_CLIENT.execute(httpPost).getEntity());
                return JSON.parseObject(result, ResultImpl.class);
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
            ;
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
        String serverUrl = japiClientStorage.getProject().getProperties().get("japi.server");
        File logoFile = getLogoFile(japiClientStorage);
        if (logoFile.exists()) {
            datas.add(new String[]{"type", "logo"});
            Result md5Result = postValues(serverUrl + "/project/md5", datas);
            if (md5Result.getData() == null) {
                postFile(serverUrl + "/transfer/projectInfo", datas, logoFile);
                postFile(serverUrl + "/transfer/projectInfo", datas, getLogoMd5File(japiClientStorage));
            } else {
                try {
                    String logoFileMd5Str = FileUtils.readFileToString(getLogoMd5File(japiClientStorage), Charset.forName("utf-8"));
                    if (!logoFileMd5Str.equals(md5Result.getData().toString())) {
                        postFile(serverUrl + "/transfer/projectInfo", datas, logoFile);
                        postFile(serverUrl + "/transfer/projectInfo", datas, getLogoMd5File(japiClientStorage));
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
        String serverUrl = japiClientStorage.getProject().getProperties().get("japi.server");
        File projectFile = new File(prePath + "/project-info.txt");
        File projectMd5File = new File(prePath + "/project-md5.txt");
        if (projectFile.exists()) {
            datas.add(new String[]{"type", "project"});
            Result md5Result = postValues(serverUrl + "/project/md5", datas);
            if (md5Result.getData() == null) {
                postFile(serverUrl + "/transfer/projectInfo", datas, projectFile);
                postFile(serverUrl + "/transfer/projectInfo", datas, projectMd5File);
            } else {
                try {
                    String logoFileMd5Str = FileUtils.readFileToString(projectMd5File, Charset.forName("utf-8"));
                    if (!logoFileMd5Str.equals(md5Result.getData().toString())) {
                        postFile(serverUrl + "/transfer/projectInfo", datas, projectFile);
                        postFile(serverUrl + "/transfer/projectInfo", datas, projectMd5File);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            postFile(serverUrl + "/transfer/projectInfo", datas, projectFile);
            postFile(serverUrl + "/transfer/projectInfo", datas, projectMd5File);
        }
    }

    private static final FileFilter FILE_FILTER = DirectoryFileFilter.DIRECTORY;
    public void autoTransfer(JapiClientStorage japiClientStorage) {
        String serverUrl = japiClientStorage.getProject().getProperties().get("japi.server");
        String projectName = japiClientStorage.getProject().getProperties().get("japi.name");
        List<String[]> datas = new ArrayList<>();
        datas.add(new String[]{"projectName", projectName});
        Result result = postValues(serverUrl + "/project/exists", datas);
        if (!result.getData().equals(Boolean.TRUE)) {//project exist
            postValues(serverUrl + "/transfer/project", datas);
        }

        transferProjectLogo(japiClientStorage);//project logo
        transferProjectInfo(japiClientStorage);//project info

        JapiNavRoot japiNavRoot = new JapiNavRoot();
        String projectPath = japiClientStorage.getJapiPath() + projectName;
        File projectFile = new File(projectPath);

        for (File packageFile : projectFile.listFiles(FILE_FILTER)) {
            if(packageFile.isHidden()){
                continue;
            }
            JapiNavPackage japiNavPackage = new JapiNavPackage();
            japiNavPackage.setName(packageFile.getName());
            for (File funFile : packageFile.listFiles(FILE_FILTER)) {
                if(funFile.isHidden()){
                    continue;
                }
                JapiNavFun japiNavFun = new JapiNavFun();
                japiNavFun.setName(funFile.getName());
                for (File actionFile : funFile.listFiles(FILE_FILTER)) {
                    if(actionFile.isHidden()){
                        continue;
                    }
                    JapiNavAction japiNavAction = new JapiNavAction();
                    japiNavAction.setName(actionFile.getName());
                    for (File versionFile : actionFile.listFiles(FILE_FILTER)) {
                        if(versionFile.isHidden()){
                            continue;
                        }
                        JapiNavVersion japiNavVersion = new JapiNavVersion();
                        japiNavVersion.setName(versionFile.getName());
                        for (File dateFile : versionFile.listFiles(FILE_FILTER)) {
                            if(dateFile.isHidden()){
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
                                Result md5Result = postValues(serverUrl + "/project/md5", das);
                                if (md5Result.getData() == null) {
                                    File infoFile = new File(projectPath + "/" + japiNavPackage.getName() + "/" + japiNavFun.getName() + "/" + japiNavAction.getName() + "/" + japiNavVersion.getName() + "/" + japiNavDate.getName() + "/info.txt");
                                    postFile(serverUrl + "/transfer/actionInfo", das, infoFile);
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
