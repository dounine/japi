package com.dounine.japi;

import com.dounine.japi.utils.FilePath;

import java.io.File;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ike on 16-10-15.
 */
public class ApplicationClient {

    public static void startClient(String serverAddress,String weProjectbName, String webJavaPath, String webPackagePath, String clientJspSavePath) throws Exception {
        Socket clients = new Socket(serverAddress.split(":")[0], Integer.parseInt(serverAddress.split(":")[1]));
        System.out.println("建立连接....");

        File file = new File(webJavaPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File file1 = new File(clientJspSavePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        FilePath filePath = new FilePath();
        filePath.setWeProjectbName(weProjectbName);
        filePath.setFileList(webJavaPath);
        filePath.setEntityList(webPackagePath);
        filePath.setClientHtmlPath(clientJspSavePath);

        InterfaceDoc interfaceDoc = new InterfaceDoc();
        JspdocContentSend.sendJspContent(interfaceDoc, filePath, null);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {  //定时发送心跳
            @Override
            public void run() {
                new Thread(new ClientHeartTask(clients, filePath)).start();
            }
        }, 0, 1000 * 20);
    }

}

