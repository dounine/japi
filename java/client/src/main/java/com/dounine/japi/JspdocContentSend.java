package com.dounine.japi;


import com.dounine.japi.utils.AddGuideMd5;
import com.dounine.japi.utils.FilePath;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ike on 16-10-21.
 */
public class JspdocContentSend {
    private static String webProjectName;
    private static String webFilePath;
    private static String filePackage;
    private static String htmlPath;

    public static void sendJspContent(InterfaceDoc interfaceDoc, FilePath filePaths, List<String> listnewDocName) throws IOException {
//        StringBuffer sb = new StringBuffer("");
        webProjectName = filePaths.getWeProjectbName();
        webFilePath = filePaths.getFileList();
        filePackage = filePaths.getEntityList();
        htmlPath = filePaths.getClientHtmlPath();
        String jspDoc = interfaceDoc.FirstMethod(webProjectName, webFilePath, filePackage, htmlPath, listnewDocName);
        AddGuideMd5.createMd5ForGuideJsp(htmlPath , webProjectName);
        //传给服务器的jsp路径以及jsp内容
//
    }

    public static void AllFileAndContent(Socket clients, String filepathstr,String savePath, String webProjectName) throws IOException {
        List<String> list = new ArrayList<>();
        File filepath = new File(filepathstr);
        List<String> listFiles = getAllFile(filepath, list);
        InputStream is = clients.getInputStream();
        OutputStream os = clients.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        DataInputStream dis = new DataInputStream(is);
        if(listFiles.size()>0){
            for (String fileString : listFiles) {
                String relative = relativePath(fileString,savePath); //  dnn/web/admin/admin.html
                dos.writeUTF(webProjectName+"/"+relative);    //传给服务器文件相对路径
                dos.flush();
                File file = new File(fileString);
                //获取内容
                InputStream content = readJspDocIs(file );
                System.out.println("客户端:" + fileString);
                dos.writeLong(file.length());
                byte[] temp = new byte[1024];
                int len = -1;
                while ((len = content.read(temp)) != -1) {      //  传给服务器文件内容
                    dos.write(temp, 0, len);
                }
                dos.flush();
                content.close();
                dis.readUTF();
            }
            dos.writeUTF("finish");
            dos.flush();
        }
    }
    ///home/ike/java/github/japi/java/server/src/main/webapp/
    // WEB-INF/views/interfaceapidoc/demo-card-consumer/com/bjike/goddess/card/action/Card1Action.html
    public static String relativePath(String absolutePath,String savePath) {
        return absolutePath.substring(savePath.length()+1);
    }

    public static List<String> getAllFile(File file, List<String> list) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.isDirectory()) {
                if (f.toString().endsWith(".html")) {
                    list.add(f.getPath());
                }
            } else {
                getAllFile(f, list);
            }

        }
        return list;
    }

    public static InputStream readJspDocIs(File f) throws FileNotFoundException, IOException {
        InputStream fis = new FileInputStream(f);
        return fis;
    }


}
