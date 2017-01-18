package com.dounine.japi;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by ike on 16-10-15.
 */
public class Task{

    private String serverIndexHtmlPath;

    /**
     * 构造函数
     */
    public Task(Socket socket ,String serverIndexHtmlPath) {
        this.serverIndexHtmlPath =serverIndexHtmlPath;
    }

    public static String updateVersionAndDate( String content ,String guideJsp, String dateOrVersionStr  , String date){
        int lens = content.indexOf( dateOrVersionStr);
        String rightStr = content.substring(lens+dateOrVersionStr.length() , content.length());
        StringBuffer findTimeStr = new StringBuffer();
        for (int i = 0,len = rightStr.length(); i < len; i++) {
            char c = rightStr.charAt(i);
            findTimeStr.append(c);
            if(findTimeStr.toString().contains("</span>")){
                break;
            }
        }
        dateOrVersionStr = dateOrVersionStr+findTimeStr.toString();
        if( StringUtils.isNotEmpty(date) && StringUtils.isNotBlank(date)){
            content = content.substring(0,lens)
                    + "<span id='"+guideJsp+"_date'>"+date+"</span>"
                    +content.substring(lens+dateOrVersionStr.length() , content.length());
        }else{
            String version = findTimeStr.toString().replaceAll("</span>","");
            int versionint = Integer.parseInt(version.trim())+1;
            content = content.substring(0,lens)
                    + "<span id='"+guideJsp+"_version'>"+versionint+"</span>"
                    +content.substring(lens+dateOrVersionStr.length() , content.length());
        }

        return content;
    }
    public static String indexJspDeal(String content ,String guideJsp,String projectName){
        String updateVersion = "<span my-attr-version='jsp-version' id='"+guideJsp+"_version'>";
        String updateTimeStr = "<span id='"+guideJsp+"_date'>";
        String guide = "<div class='btn' id='mydoc' doc-Attr='"+projectName+"'><a href='/interfaceapidoc/index/"+projectName+"'>"+projectName+"</a></div>";
        String guideJspDealNew = "<div class='new' id='"+guideJsp.trim()+"'>新</div>";
        String guideJspDealOld = "<div class='old' id='" + guideJsp.trim() + "'></div>";
        String date = LocalDate.parse(String.valueOf(LocalDate.now()), DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
        if( content.contains(guide)){
            if (content.contains(guideJspDealOld) ) {
                int len = content.indexOf(guideJspDealOld);
                content = content.substring(0, len)
                        + guideJspDealNew
                        + content.substring(len + guideJspDealOld.length(), content.length());
            }else if (!content.contains(guideJspDealOld) && !content.contains(guideJspDealNew) ) {
                int len = content.indexOf(guide);
                content = content.substring(0, len + guide.length())
                        + guideJspDealNew
                        + content.substring(len + guide.length(), content.length());
            }

            content = updateVersionAndDate(content ,guideJsp ,  updateTimeStr , date);
            content = updateVersionAndDate(content ,guideJsp ,  updateVersion , null);
        }else{
            String newUl = "</div><div class='mainbody'><ul>";
            int newUlIndex = content.indexOf(newUl);
            StringBuffer indexJspNewUl = new StringBuffer("");

            indexJspNewUl.append("<li> " +
                    "                <i class='iconfont'>&#xe605;</i> " +
                    "                <p>接口文档</p> " +
                    "                <div class='list'> " +
                    "                    <span> " +
                    "                        <i class='iconfont '>&#xe600;</i> " +
                    "                        <span>V<span my-attr-version='jsp-version' id='"+guideJsp+"_version'>0</span></span> " +
                    "                    </span> " +
                    "                    <span style='border-left:1px solid #999;padding-left:5px'> " +
                    "                        <i class='iconfont '>&#xe601;</i> " +
                    "                        <span id='"+guideJsp+"_date'>"+date+"</span> " +
                    "                    </span>" +
                    "                </div> " +
                    "                <div class='btn' id='mydoc' doc-Attr='"+projectName+"'> " +
                    "                    <a href='/interfaceapidoc/index/" + projectName + "'>" + projectName + "文档</a> " +
                    "                </div>" +
                    "                <div class='new' id='" + guideJsp + "'>新</div> " +
                    "           </li> ");
            content = content.substring(0, newUlIndex + newUl.length())
                    + indexJspNewUl.toString()
                    + content.substring(newUlIndex + newUl.length(), content.length());
        }

        return content;
    }

    public String dirCreate(String serverIndexHtmlPath , String readStr) throws IOException {
        File serverPath = new File(serverIndexHtmlPath);
        if(!serverPath.isDirectory()){
            serverPath.mkdir();
        }
        String[] pathSuffixSplits = readStr.split("/");
        StringBuffer path = new StringBuffer(serverIndexHtmlPath);
        for (String pathSuffixSplit : pathSuffixSplits) { //建立目录
            path.append("/" + pathSuffixSplit);
            if (!pathSuffixSplit.contains(".")) {
                File f = new File(path.toString());
                if (!f.isDirectory() && !f.exists()) {
                    f.mkdir();
                }
            }
        }
        return path.toString();
    }
}
