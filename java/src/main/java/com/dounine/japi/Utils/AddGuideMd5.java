package com.dounine.japi.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ike on 16-11-1.
 */
public class AddGuideMd5 {

    public static  void createMd5ForGuideJsp( String htmlPath , String projectName) {
        List<String> list = new ArrayList<>();
        File file = new File(htmlPath);
        list = GetAllFIle.getAllFile(file, list);
        for (String str : list) {//GUIDE.JSP
            String[] guideFlag = str.split(htmlPath);
            String[] guide = guideFlag[1].split("/");
            if (guide.length > 2 && str.endsWith(".jsp")) { //说明不是guide.jsp
                FileMD5Util fileMD5 = new FileMD5Util();
                String jspFilemd5 = fileMD5.getFileMD5(new File(str));
                //修改guide.jsp的ｍｄ5
                guideJspAddMd5(str, htmlPath + "/" + projectName + "guide.jsp", jspFilemd5);

            }
        }
    }

    public static void guideJspAddMd5 ( String str , String guidepath , String javaFileChange){
        try {
            String idStr = str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf(".jsp"));
            String matchId = "id='" + idStr + "'";
            String guideStr = FileUtils.readFileToString(new File(guidepath));
            String guideHtmlStr = guideStr.substring(0, guideStr.indexOf(matchId))
                    + "id='" + idStr + "' my-attr-md5='" + javaFileChange + "'"
                    + guideStr.substring(guideStr.indexOf(matchId) + matchId.length(), guideStr.length());
            FileOutputStream fileoutputstream = null;// 建立文件输出流

            fileoutputstream = new FileOutputStream(guidepath);
            byte tag_bytes[] = guideHtmlStr.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();//关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
