package com.dounine.japi;

import com.dounine.japi.utils.AddGuideMd5;
import com.dounine.japi.utils.FileMD5Util;
import com.dounine.japi.utils.FilePath;
import com.dounine.japi.utils.GetAllFIle;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ike on 16-10-19.
 */
public class ClientDocCheckTask {

    public List<String> checkDoc(FilePath filePath,List<String> addFilePaths) {
        String projectName = filePath.getWeProjectbName();
        String webFilePath = filePath.getFileList();
        String filePackage = filePath.getEntityList();
        String htmlPath = filePath.getClientHtmlPath();

        List<String> editFilePaths = new ArrayList<>();


        String guidepath ="";
        boolean flag = false;
        String firstFlag = "";
        List<String> listnewDocName = new ArrayList<>();
        File file = new File(htmlPath);
        List<String> listFiles = new ArrayList<>();
        GetAllFIle.getAllFile(file, listFiles);//all files
        try {
            for (String path : listFiles) {//GUIDE.html
                String[] guideFlag = path.split(htmlPath);//com/xx/xx.html  guide.html
                String[] guide = guideFlag[1].split("/");
                if (guide.length > 2 && path.endsWith(".html")) { //说明不是guide.html
                    String filePackagetoPath = filePackage.replace(".", "/");
                    filePackagetoPath = path.substring(path.indexOf(filePackagetoPath), path.lastIndexOf(".")).trim();
                    FileMD5Util fileMD5 = new FileMD5Util();
                    String htmlPathtxt = htmlPath+"/" + filePackagetoPath + ".txt";
                    String txtContent = readDoc(htmlPathtxt);
                    String[] strmd5 = txtContent.split("-date:");//读取txt文档获取之前的md5值
                    String javaFileChange = fileMD5.getFileMD5(new File(path));//get file md5
                    if (!javaFileChange.equals(strmd5[0])) {//two file not equals(update)
                        editFilePaths.add(path);
                        //listnewDocName.add(filePath);
                        //重新写入修改后的md5
                        writeDoc(htmlPathtxt, javaFileChange);
                    }

//                    if( strmd5[1].endsWith("first")){//说明前面是第一次创建
//                        firstFlag = "first";
//                    }
                    //修改guide.html的ｍｄ5
                    AddGuideMd5.guideJspAddMd5 (  path ,  htmlPath+"/__index.html" ,  javaFileChange);

                }
            }
            InterfaceDoc.initDocs(filePath.getWeProjectbName(),filePath.getFileList(),filePath.getEntityList() ,filePath.getClientHtmlPath(),addFilePaths);
//            if( "first".equals(firstFlag)){
//                flag =true;
//            }
//            if((listnewDocName !=null && listnewDocName.size()>0)  ){ //表示有新的改变
//                 JspdocContentSend.sendJspContent(interfaceDoc, filePaths, listnewDocName);
//                flag =true;
//            }else{
//                flag = false;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return editFilePaths;
    }



    public void writeDoc(String path ,String newFileMD5) throws IOException{
        BufferedWriter bos = new BufferedWriter(new FileWriter( path ));
        StringBuilder sb = new StringBuilder("");
        bos.write(sb.toString());
        bos.write(" "+newFileMD5+"-date:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        bos.close();
    }

    public String readDoc(String path) throws IOException{
        return FileUtils.readFileToString(new File(path),"utf-8");
    }


}
