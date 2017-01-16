package com.dounine.japi;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ike on 16-10-15.
 */
public class ApplicationServer {

    public static void serverStart(int listenerPort, String serverJspSavePath )throws IOException {

        final ServerSocket server = new ServerSocket(listenerPort);

        File f = new File(serverJspSavePath);
        String[] flist = f.list();
        IndexTask indexTask = new IndexTask();
        indexTask.createindex(serverJspSavePath, flist, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    Socket socket = null;
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 每接收到一个Socket就建立一个新的线程来处理它
                    new Thread(new Task(socket  ,serverJspSavePath )).start();

                }
            }
        }).start();

    }



}
