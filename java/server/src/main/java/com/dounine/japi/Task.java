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
public class Task implements Runnable {

    private Socket socket;
    private String serverIndexHtmlPath;

    /**
     * 构造函数
     */
    public Task(Socket socket ,String serverIndexHtmlPath) {
        this.socket = socket;
        this.serverIndexHtmlPath =serverIndexHtmlPath;
    }

    @Override
    public void run() {
        try {
            handlerSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跟客户端Socket进行通信
     *
     *
     */
    private void handlerSocket() throws Exception {
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        DataOutputStream dos = new DataOutputStream(os);
        for(;;) {
            String status = dis.readUTF();
            System.out.println("状态:" + status);
            switch (status) {
                case "live":
                    //TODO send live-confirm
                    System.out.println("task0:" + status);
                    dos.writeUTF("live-confirm");
                    dos.flush();
                    break;
                case "update-doc":
                    //TODO 1.
                    System.out.println("task1:" + status);
                    dos.writeUTF("update-receive");
                    dos.flush();
                    break;
                case "send-ready":
                    dos.writeUTF("file-receive");
                    dos.flush();
                    String fileName = "";
                    String dirCreates = "";
                    String guidepath ="";
                    String guideName ="";
                    while (!(fileName = dis.readUTF()).equals("finish")) {
                        dirCreates = dirCreate(serverIndexHtmlPath, fileName);
                        guidepath = dirCreates.substring( dirCreates.lastIndexOf("/")+1, dirCreates.length());
                        if(guidepath.split("guide.html").length==1){
                            guideName = guidepath;
                        }
                        FileOutputStream fos = new FileOutputStream(new File(dirCreates));

                        long fileLength = dis.readLong();  //多文件长度必须写
                        byte[] receive = new byte[1024];
                        int len = -1;
                        long readSum = 0;
                        while (readSum < fileLength) {
                            len = dis.read(receive);
                            readSum += len;
                            fos.write(receive, 0, len);
                        }
                        fos.flush();
                        fos.close();
                        dos.writeUTF("file-finish");
                        fos.flush();
                    }

                    String indexJspContent = FileUtils.readFileToString(new File(serverIndexHtmlPath + "/index.html"));
                    String Jspcontents = indexJspDeal(indexJspContent,guideName);//"feedbackguide.html"
                    FileOutputStream fileoutputstream = null;// 建立文件输出流
                    try {
                        fileoutputstream = new FileOutputStream(serverIndexHtmlPath + "/index.html");
                        byte tag_bytes[] = Jspcontents.getBytes();
                        fileoutputstream.write(tag_bytes);
                        fileoutputstream.close();//关闭输出流
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public String updateVersionAndDate( String content ,String guideJsp, String dateOrVersionStr  , String date){
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
    public String indexJspDeal(String content ,String guideJsp){
        String updateVersion = "<span my-attr-version='jsp-version' id='"+guideJsp+"_version'>";
        String updateTimeStr = "<span id='"+guideJsp+"_date'>";
        String guide = "<div class='btn' id='mydoc' doc-Attr='feedback'><a href='/interfaceapidoc/"+guideJsp.trim()+"'>"+guideJsp.substring(0,guideJsp.lastIndexOf("guide.html"))+"文档</a></div>";
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
                    "                <div class='btn' id='mydoc' doc-Attr='"+guideJsp.substring(0, guideJsp.lastIndexOf("guide.html"))+"'> " +
                    "                    <a href='/interfaceapidoc/" + guideJsp + "'>" + guideJsp.substring(0, guideJsp.lastIndexOf("guide.html")) + "文档</a> " +
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
