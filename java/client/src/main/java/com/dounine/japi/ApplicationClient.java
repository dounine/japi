package com.dounine.japi;

import com.dounine.japi.utils.FilePath;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ike on 16-10-15.
 */
public class ApplicationClient {

    public static void startClient(String projectName, String actionRootPath, String packages, Class<?> clazz){
        String htmlSavePath = clazz.getResource("/").getPath()+"html";
        File file1 = new File(htmlSavePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        FilePath filePath = new FilePath();
        filePath.setWeProjectbName(projectName);
        filePath.setFileList(actionRootPath);
        filePath.setEntityList(packages);
        filePath.setClientHtmlPath(htmlSavePath);

//        InterfaceDoc interfaceDoc = new InterfaceDoc();
//        try {
//            JspdocContentSend.sendJspContent(interfaceDoc, filePath, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        new ClientHeartTask(filePath,htmlSavePath).exec();
    }

}

