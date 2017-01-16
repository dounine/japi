package csio.client;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ike on 16-10-15.
 */
public class ClientTest {

    @Test
    public void startClient(){
        String weProjectbName ="feedback";
        String webJavaPath = "/Users/huanghuanlai/dounine/github/japi/java/src/main/java/com/dounine/japi/web";
        String webPackagePath ="com.dounine.japi.web";
        String clientJspSavePath ="/Users/huanghuanlai/dounine/github/japi/java/src/main/webapp/WEB-INF/views/interfaceapidoc";
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            ApplicationClient.startClient("127.0.0.1:8989",weProjectbName,webJavaPath,webPackagePath,clientJspSavePath);
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

