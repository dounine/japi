package com.dounine.japi.utils;

/**
 * Created by ike on 16-10-19.
 */
public class FilePath {
    private String weProjectbName;
    private String  fileList;  //web路径
    private String  entityList; //web文件的包名 package xxx
    private String  clientHtmlPath; //该应用的html地址
    private String  serverIndexHtmlPath;//服务器的html地址

    public String getWeProjectbName() {
        return weProjectbName;
    }

    public void setWeProjectbName(String weProjectbName) {
        this.weProjectbName = weProjectbName;
    }

    public String getFileList() {
        return fileList;
    }

    public void setFileList(String fileList) {
        this.fileList = fileList;
    }

    public String getEntityList() {
        return entityList;
    }

    public void setEntityList(String entityList) {
        this.entityList = entityList;
    }

    public String getClientHtmlPath() {
        return clientHtmlPath;
    }

    public void setClientHtmlPath(String clientHtmlPath) {
        this.clientHtmlPath = clientHtmlPath;
    }

    public String getServerIndexHtmlPath() {
        return serverIndexHtmlPath;
    }

    public void setServerIndexHtmlPath(String serverIndexHtmlPath) {
        this.serverIndexHtmlPath = serverIndexHtmlPath;
    }
}
