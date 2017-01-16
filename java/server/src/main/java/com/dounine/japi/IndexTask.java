package com.dounine.japi;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by ike on 16-10-17.
 */
public class IndexTask {
    public void createindex(String path ,String [] indexNames ,String guidePath ){
//        mkdirServer(path);
        String content = createIndexhtml(  indexNames ,guidePath );
        FileOutputStream fileoutputstream = null;// 建立文件输出流
        try {
            fileoutputstream = new FileOutputStream(path+"/index.html");
            byte tag_bytes[] = content.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();//关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public void mkdirServer( String path){
//        ///home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc/index.html
//        File f = new File(path.trim());
//        if (!f.isDirectory()) {
//            f.mkdir();
//        }
//    }
    public String createIndexhtml( String[] indexNames ,String guidePath ) {
        StringBuffer sbu = new StringBuffer("");
        sbu.append("<!DOCTYPE html>     " +
                "<html lang='en'>     " +
                "<head>     " +
                "    <meta charset='UTF-8'>     " +
                "    <title>文档</title>     " +
                "    <link rel='stylesheet' href='${ctx}/html/css/index_red.css'>     " +
                "    <script src='${ctx}/html/js/jquery.min.js'></script>     " +
                "    <script src='${ctx}/html/js/index.js'></script>     " +
                "</head>")
                .append("<body>")
                .append("<header>     " +
                        "        <div class='logo'><a href='javascript:void(0) '>     " +
                        "            <img src='${ctx}/html/img/logo.png' ></a></div>     " +
                        "        <div class='changeColor'>     " +
                        "            <a href='javascript:;' class='blue' style='background:#238DFA'></a>     " +
                        "            <a href='javascript:;' class='yellow' style='background:#FBE786'></a>     " +
                        "            <a href='javascript:void(0)' class='green' style='background:#22CB56'></a>     " +
                        "            <a href='javascript:void(0)' class='red' style='background:#F65866;display:none' ></a>     " +
                        "        </div>     " +
                        "        <div class='search'><input type='text'><a href='javascript:;'>搜索</a></div>     " +
                        "    </header>")
                .append("<div class='notice'>     " +
                        "        <i class='iconfont'>&#xe604;</i><span>公告:一波doc僵尸即将来临!!</span>     " +
                        "    </div>")
                .append("<div class='mainbody'>")
                .append("<ul>");
        if( indexNames != null && indexNames.length>0){
            String date = LocalDate.parse(String.valueOf(LocalDate.now()), DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
            for (String indexName : indexNames) {
                if( !"index.html".equals(indexName) && indexName.contains(".jsp")){
                    sbu.append("<li>     " +
                            "                <i class='iconfont'>&#xe605;</i>     " +
                            "                <p>接口文档</p>     " +
                            "                <div class='list'>     " +
                            "                    <span>     " +
                            "                        <i class='iconfont '>&#xe600;</i>     " +
                            "                        <span>V<span my-attr-version='jsp-version' id='"+indexName+"_version'>0</span></span>     " +
                            "                    </span>     " +
                            "                    <span style='border-left:1px solid #999;padding-left:5px'>     " +
                            "                        <i class='iconfont '>&#xe601;</i>     " +
                            "                        <span id='"+indexName+"_date'>"+date+"</span>     " +
                            "                    </span>     " +
                            "                </div>     " +
                            "                <div class='btn' id='mydoc' doc-Attr='"+indexName.substring(0,indexName.lastIndexOf("guide.jsp"))+"'><a href='/views/interfaceapidoc/"+indexName+"'>" + indexName.substring(0,indexName.lastIndexOf("guide.jsp")) + "文档</a></div>     ");
                    String needUpdateDoc = "/"+indexName.trim();
                    if(StringUtils.isNotEmpty(guidePath) && StringUtils.isNotBlank(guidePath)){
                        if( needUpdateDoc.equals(guidePath.trim())){
                            sbu.append("                <div class='new' id='"+indexName.trim()+"'>新</div>     " );
                        }else{
                            sbu.append("                <div class='old' id='"+indexName.trim()+"'></div>     " );
                        }
                    }
                    sbu.append("           </li>");
                }
            }
        }

        sbu.append(" </ul>     " )
                .append("    </div>")
                .append("<!-- <footer>     " +
                        "        <a href='javascript:;' class='previous '><img src='${ctx}/html/img/previous.png'></a>     " +
                        "        <ul class='page'>     " +
                        "            <li ><a href='javascript:;' class='active'>1</a></li>     " +
                        "            <li><a href='javascript:;'>2</a></li>     " +
                        "            <li><a href='javascript:;'>3</a></li>     " +
                        "            <li><a href='javascript:;'>4</a></li>     " +
                        "            <li><a href='javascript:;'>5</a></li>     " +
                        "            <li><a href='javascript:;'>6</a></li>     " +
                        "        </ul>     " +
                        "        <a href='javascript:;' class='next'><img src='${ctx}/html/img/next.png'></a>     " +
                        "        <div class='jump'>跳转到：<input type='text' size='2'>     " +
                        "            <a href='javascript:;'>GO</a>     " +
                        "        </div>     " +
                        "    </footer> --> ")
                .append(" </body>     " +
                        " </html> ");
        return sbu.toString();
    }
}
