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

    public static void serverStart(String serverJspSavePath )throws IOException {

        File f = new File(serverJspSavePath);
        String[] flist = f.list();
        IndexTask indexTask = new IndexTask();
        indexTask.createindex(serverJspSavePath, flist, null);

    }



}
