package com.dounine.japi.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by ike on 16-10-28.
 */
public class JspFileDealUtil {
    public static  String mkHtmlDir(String webProjectName ,String filePath, String pckPath, String fileName) {
        String[] pckPaths = pckPath.split("/");
        String contextPath = "";
        File filePathFile = new File(filePath);
        if (!filePathFile.exists()) {
            filePathFile.mkdirs();
        }
        ///home/ike/java/github/japi/java/server/src/main
        if( StringUtils.isNotBlank(webProjectName )){//把项目名加上去
            //filePath = filePath;
            filePathFile = new File(filePath);
            if (!filePathFile.isDirectory()) {
                filePathFile.mkdir();
            }
        }
        for (String pckPathStr : pckPaths) {
            contextPath = contextPath + "/" + pckPathStr;
            String filePathContent = filePath.trim() + "/" + contextPath.trim();
            File f = new File(filePathContent);
            if (!f.isDirectory()) {
                f.mkdir();
            }
        }

        String paths = filePath + "/" + pckPath + "/" + fileName;
        return paths;
    }


    public static void htmlTxtFile(String jspPaths) {
        String fileame = ".txt";
        fileame = jspPaths + fileame;//生成的html文件保存路径。

        File f = new File(fileame);
        if( !f.exists() ){
            FileOutputStream fileoutputstream = null;// 建立文件输出流
            try {
                fileoutputstream = new FileOutputStream(fileame);
                String content = FileMD5Util.getFileMD5(new File(jspPaths + ".html"));
                content =" " +content+"-date:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                byte tag_bytes[] = content.getBytes();
                fileoutputstream.write(tag_bytes);
                fileoutputstream.close();//关闭输出流
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public static void htmls(String paths, String fileContents) {
        String fileame = ".html";
        fileame = paths + fileame;//生成的html文件保存路径。
        FileOutputStream fileoutputstream = null;// 建立文件输出流
        try {
            fileoutputstream = new FileOutputStream(fileame);
            byte tag_bytes[] = fileContents.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();//关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
