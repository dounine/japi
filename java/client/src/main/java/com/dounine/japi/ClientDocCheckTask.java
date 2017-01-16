package com.dounine.japi;

import com.dounine.japi.utils.AddGuideMd5;
import com.dounine.japi.utils.FileMD5Util;
import com.dounine.japi.utils.FilePath;
import com.dounine.japi.utils.GetAllFIle;

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

    public boolean checkDoc( Socket clients , FilePath filePaths ) {
        String projectName = filePaths.getWeProjectbName();
        String webFilePath = filePaths.getFileList();
        String filePackage = filePaths.getEntityList();
        String htmlPath = filePaths.getClientHtmlPath();

        InterfaceDoc interfaceDoc = new InterfaceDoc();
        interfaceDoc.FirstMethod(projectName,webFilePath ,filePackage ,htmlPath , null );


        String guidepath ="";
        boolean flag = false;
        List<String> list = new ArrayList<>();
        List<String> listnewDocName = new ArrayList<>();
        File file = new File(htmlPath);
        list = GetAllFIle.getAllFile(file, list);
        try {
            for (String str : list) {//GUIDE.JSP
                String [] guideFlag = str.split(htmlPath);
                String[] guide = guideFlag[1].split("/");
                if (guide.length > 2 && str.endsWith(".html")) { //说明不是guide.jsp
                    String filePackagetoPath = filePackage.replace(".", "/");
                    filePackagetoPath = str.substring(str.indexOf(filePackagetoPath), str.lastIndexOf(".")).trim();
                    FileMD5Util fileMD5 = new FileMD5Util();
                    String htmlPathtxt = htmlPath + "/"+projectName+"/" + filePackagetoPath + ".txt";
                    String txtContent = readDoc(htmlPathtxt);
                    String javaFileChange = fileMD5.getFileMD5(new File(str));
                    if (!javaFileChange.equals(txtContent)) {
                        listnewDocName.add(str);
                        //重新写入修改后的md5
                        writeDoc(htmlPathtxt, javaFileChange);
                    }

                    //修改guide.jsp的ｍｄ5
                    AddGuideMd5.guideJspAddMd5 (  str ,  htmlPath+"/"+projectName+"guide.html" ,  javaFileChange);

                }
            }
            if(listnewDocName !=null && listnewDocName.size()>0 ){ //表示有新的改变
                 JspdocContentSend.sendJspContent(interfaceDoc, filePaths, listnewDocName);
                flag =true;
            }else{
                flag = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }



    public void writeDoc(String path ,String newFileMD5) throws IOException{
        BufferedWriter bos = new BufferedWriter(new FileWriter( path ));
        StringBuilder sb = new StringBuilder("");
        bos.write(sb.toString());
        bos.write(" "+newFileMD5+"-date:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        bos.close();
    }

    public String readDoc(String path) throws IOException{
        BufferedReader bis = new BufferedReader(new FileReader( path ));
        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<String>();
        while (bis.read() != -1) {
            lines.add(bis.readLine());
        }
        for (String s : lines) {
            sb.append(s);
        }
        String[] strmd5 = sb.toString().split("-date:");
        return strmd5[0];
    }


}
