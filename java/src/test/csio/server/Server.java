package csio.server;

import com.dounine.japi.Utils.FilePath;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ike on 16-10-15.
 */
public class Server {
   /* @Test
    public void test() throws IOException{
        FilePath filePath = new FilePath();
//        filePath.setServerIndexHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
        filePath.setServerIndexHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
        serverStart(filePath.getServerIndexHtmlPath());
    }*/

    public static void main(String[] args) throws IOException{
        FilePath filePath = new FilePath();
//        filePath.setServerIndexHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
        filePath.setServerIndexHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
        serverStart(filePath.getServerIndexHtmlPath());
    }

    public static void serverStart(String serverJspSavePath )throws IOException {
        int port = 8899;

        final ServerSocket server = new ServerSocket(port);
        System.out.println("等待与客户端建立连接...");

        File f = new File(serverJspSavePath);
        String[] flist = f.list();
        IndexTask indexTask = new IndexTask();
        indexTask.createindex(serverJspSavePath, flist, null);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    Socket socket = null;
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 每接收到一个Socket就建立一个新的线程来处理它
                    new Thread(new Task(socket  ,serverJspSavePath )).start();

                }
            }
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {

        }
    }



}
