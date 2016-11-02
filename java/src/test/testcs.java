import csio.client.Client;
import com.dounine.japi.Utils.FilePath;
import csio.server.Server;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ike on 16-11-2.
 */
public class testcs {
    @Test
    public void test(){
        FilePath filePath = new FilePath();
        filePath.setWeProjectbName("feedback");
        filePath.setFileList("/home/ike/java/java/feedback/java/src/main/java/dnn/web1");
        filePath.setEntityList("dnn.web1");
        filePath.setClientHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc");
        filePath.setServerIndexHtmlPath("/home/ike/java/java/feedback/java/src/main/webapp/views/interfaceapidoc");
        try {
            Server.serverStart(filePath.getServerIndexHtmlPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void test1() throws  Exception{
        String weProjectbName ="feedback";
        String webJavaPath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web1";
        String webPackagePath ="dnn.web1";
        String clientJspSavePath ="/home/ike/java/java/feedback/java/src/main/webapp/WEB-INF/views/interfaceapidoc";
        Client.startClient(weProjectbName , webJavaPath ,webPackagePath ,clientJspSavePath );
    }
}
