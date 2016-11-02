package csio.client;


import com.dounine.japi.InterfaceDoc;
import com.dounine.japi.Utils.AddGuideMd5;
import com.dounine.japi.Utils.FilePath;

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
//        AllFileAndContent(htmlPath , sb);
//        return sb.toString().getBytes();
    }

    public static void AllFileAndContent(Socket clients, String filepathstr, String webProjectName) throws IOException {
        List<String> list = new ArrayList<>();
        File filepath = new File(filepathstr);
        List<String> listFiles = getAllFile(filepath, list);
        InputStream is = clients.getInputStream();
        OutputStream os = clients.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        DataInputStream dis = new DataInputStream(is);
        for (String fileString : listFiles) {
            String relative = relativePath(fileString ,webProjectName); //  dnn/web/admin/admin.jsp
            dos.writeUTF(relative);    //传给服务器文件相对路径
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
    public static String relativePath(String readStr ,String webProjectName) {
        String str = "/WEB-INF/";
        int indexs = readStr.trim().lastIndexOf(str);
        String pathSuffix = readStr.trim().substring(indexs + str.length(), readStr.trim().length());
        int webIndex = pathSuffix.indexOf(webProjectName);
        pathSuffix = pathSuffix.substring(webIndex,pathSuffix.length());
        return pathSuffix;
    }

    public static List<String> getAllFile(File file, List<String> list) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.isDirectory()) {
                if (f.toString().endsWith(".jsp")) {
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
