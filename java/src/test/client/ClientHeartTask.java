package client;

import java.io.*;
import java.net.Socket;

/**
 * Created by ike on 16-10-19.
 */
public class ClientHeartTask implements Runnable {
    private Socket clients;
    private FilePath filePath;
    private ClientDocCheckTask clientDocCheckTask = new ClientDocCheckTask();
    private static int heartflag = 1;//1表示发心跳,0表示不发心跳
    private static String fistFlag="first";//说明客户端刚刚启动
//    private static byte[] bt;

    public ClientHeartTask(Socket client , FilePath filePath ) {
        this.clients = client;
        this.filePath = filePath;
    }
    @Override
    public void run() {
        try {
            InputStream is = clients.getInputStream();
            OutputStream os = clients.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
            System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息libe");
            try {
                if( heartflag == 1){
                    dos.writeUTF("live");//心跳
                }
                String status = dis.readUTF();

                switch (status){
                    case "live-confirm":
                        boolean flag = false;
                        flag = clientDocCheckTask.checkDoc(clients, filePath);
                        if ( flag==true || fistFlag.equals("first")) {
                            heartflag =0;
                            dos.writeUTF("update-doc");
                            fistFlag = "no-first";
                        }
                        break;
                    case "update-receive":
                        heartflag =0;
                        //TODO send file
                        System.out.println("client1:"+System.currentTimeMillis());
                        dos.writeUTF("send-ready");
                        System.out.println("client1:"+System.currentTimeMillis());
                        break;
                    case "file-receive":
                        heartflag =0;
                        //send fileInputStream
                        JspdocContentSend.AllFileAndContent(clients , filePath.getClientHtmlPath() ,filePath.getWeProjectbName() );
//                        dos.write(str);
                        System.out.println("传输局:"+status);
//                        System.out.println("传输局:"+str);
                        heartflag =1;
//                        dos.writeUTF("哈哈");
                        break;
                }















//                if (status.equals("update-yes")) {
//                    System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息文档");
//                    dos.writeUTF("send-ready-yes");
////                    //发送文档
////                    StringBuffer sb = new StringBuffer("");
////                    String strs = JspdocContentSend.getAllFileAndContent( filePath.getClientHtmlPath() , sb );
////                    dos.write(strs.getBytes());
//                    //dos.writeUTF( strs );
//                } else if (status.equals("update-no")) {
//                    System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息发送成功liveuno");
//
//                } else if (status.equals("receive-update-yes")) {
//                    System.out.println("Cliect[port:" + clients.getLocalPort() + "] 消息发送成功");
//                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
