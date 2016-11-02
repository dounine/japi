package client;

import com.dounine.japi.InterfaceDoc;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ike on 16-10-15.
 */
public class Client {
    public static void main(String[] args) throws Exception {
        final int heartFlag =1;//1发送心跳 ,0不发
        Socket clients = new Socket("127.0.0.1", 8899);
        System.out.println("建立连接....");

        FilePath filePath = new FilePath();
        filePath.setWeProjectbName("feedback");
        filePath.setFileList("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
        filePath.setEntityList("dnn.web1");
        filePath.setClientHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc");

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

