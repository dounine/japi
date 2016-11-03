package csio.client;

import com.dounine.japi.InterfaceDoc;
import com.dounine.japi.Utils.FilePath;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ike on 16-10-15.
 */
public class Client {
    public static void main(String[] args) throws Exception {
        String weProjectbName ="feedback";
        String webJavaPath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web1";
        String webPackagePath ="dnn.web1";
        String clientJspSavePath ="/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc";
        startClient( weProjectbName , webJavaPath , webPackagePath , clientJspSavePath);

    }

    public static void startClient( String weProjectbName , String webJavaPath , String webPackagePath , String clientJspSavePath ) throws Exception {
        Socket clients = new Socket("127.0.0.1", 8899);
        System.out.println("建立连接....");

        FilePath filePath = new FilePath();
        filePath.setWeProjectbName( weProjectbName );
        filePath.setFileList(  webJavaPath );
        filePath.setEntityList(  webPackagePath );
        filePath.setClientHtmlPath(  clientJspSavePath );

        InterfaceDoc interfaceDoc = new InterfaceDoc();
        JspdocContentSend.sendJspContent( interfaceDoc ,filePath , null);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {  //定时发送心跳
            @Override
            public void run() {
                new Thread(new ClientHeartTask(clients ,filePath )).start();
            }
        },0, 1000*20);
    }

}

