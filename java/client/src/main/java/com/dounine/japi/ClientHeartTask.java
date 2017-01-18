package com.dounine.japi;

import com.dounine.japi.utils.FilePath;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created by ike on 16-10-19.
 */
public class ClientHeartTask{
    private FilePath filePath;
    private ClientDocCheckTask clientDocCheckTask = new ClientDocCheckTask();
    private String savePath;

    public ClientHeartTask(FilePath filePath ,String savePath) {
        this.filePath = filePath;
        this.savePath = savePath;
    }

    public void exec() {
        try {
            List<String> addFilePaths =  InterfaceDoc.initDocs(filePath.getWeProjectbName(),filePath.getFileList(),filePath.getEntityList() ,filePath.getClientHtmlPath(),null);
            List<String> editFilePaths = clientDocCheckTask.checkDoc(filePath,addFilePaths);
            if ( editFilePaths.size()>0 || addFilePaths.size()>0) {
                JspdocContentSend.allFileAndContent(filePath.getClientHtmlPath(),savePath ,filePath.getWeProjectbName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
