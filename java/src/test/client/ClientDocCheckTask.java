package client;

import com.dounine.japi.InterfaceDoc;

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
//    filePath.setFileList("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
//        filePath.setEntityList("dnn.web1");
//        filePath.setClientHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc");
    //ServerIndexHtmlPath(           "/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");

    public boolean checkDoc( Socket clients , FilePath filePaths ) {
        String projectName = filePaths.getWeProjectbName();
        String webFilePath = filePaths.getFileList();
        String filePackage = filePaths.getEntityList();
        String htmlPath = filePaths.getClientHtmlPath();

        System.out.println("不定时更新减肥品");
        InterfaceDoc interfaceDoc = new InterfaceDoc();
        interfaceDoc.FirstMethod(projectName,webFilePath ,filePackage ,htmlPath , null );
        System.out.println("减肥品2");

//        byte[] jspDoc =new byte[1024];
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
//                if( guide.length <= 2 && str.endsWith(".jsp")){//是guide.jsp
//                    guidepath = str;
//                }
                if (guide.length > 2 && str.endsWith(".jsp")) { //说明不是guide.jsp
                    String filePackagetoPath = filePackage.replace(".", "/");
                    filePackagetoPath = str.substring(str.indexOf(filePackagetoPath), str.lastIndexOf(".")).trim();
                    FileMD5 fileMD5 = new FileMD5();
                    String htmlPathtxt = htmlPath + "/"+projectName+"/" + filePackagetoPath + ".txt";
                    String txtContent = readDoc(htmlPathtxt);
                    String javaFileChange = fileMD5.getFileMD5(new File(str));
                    if (!javaFileChange.equals(txtContent)) {
                        listnewDocName.add(str);
                        //重新写入修改后的md5
                        writeDoc(htmlPathtxt, javaFileChange);
                    }

                    //修改guide.jsp的ｍｄ5
                    AddGuideMd5.guideJspAddMd5 (  str ,  htmlPath+"/"+projectName+"guide.jsp" ,  javaFileChange);

                }
            }
            if(listnewDocName !=null && listnewDocName.size()>0 ){ //表示有新的改变
                 JspdocContentSend.sendJspContent(interfaceDoc, filePaths, listnewDocName);
                flag =true;
            }else{
//                jspDoc = "".getBytes();
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
