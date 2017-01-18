package com.dounine.japi;

import com.dounine.japi.utils.FilePath;

import java.io.*;
import java.net.Socket;

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
            boolean flag = clientDocCheckTask.checkDoc(filePath);
            if ( true) {
                JspdocContentSend.AllFileAndContent(filePath.getClientHtmlPath(),savePath ,filePath.getWeProjectbName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
