package com.dounine.japi;


import com.dounine.japi.utils.AddGuideMd5;
import com.dounine.japi.utils.FilePath;
import com.dounine.japi.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;

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

    public static void AllFileAndContent(String filepathstr,String savePath, String webProjectName) throws IOException {
        List<String> list = new ArrayList<>();
        File filepath = new File(filepathstr);
        List<String> listFiles = getAllFile(filepath, list);
        if(listFiles.size()>0){
            for (String fileString : listFiles) {
                if(!fileString.endsWith(webProjectName+"guide.html")){
                    String relative = relativePath(fileString.substring(0,fileString.lastIndexOf("/")),savePath); //  dnn/web/admin/admin.html
                    FileUtil.upload(webProjectName,relative+"/",new File(fileString));
                }else{
                    FileUtil.upload(webProjectName,"",new File(fileString));
                }

            }
        }
    }
    ///home/ike/java/github/japi/java/server/src/main/webapp/
    // WEB-INF/views/interfaceapidoc/demo-card-consumer/com/bjike/goddess/card/action/Card1Action.html
    public static String relativePath(String absolutePath,String savePath) {
        if(StringUtils.isNotBlank(absolutePath)){
            return absolutePath.substring(savePath.length()+1);
        }
        return null;
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
