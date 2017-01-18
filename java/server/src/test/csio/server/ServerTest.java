package csio.server;

import com.dounine.japi.utils.FilePath;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ike on 16-10-15.
 */
public class ServerTest {

    @Test
    public void testStart(){
        FilePath filePath = new FilePath();
        filePath.setServerIndexHtmlPath("/Users/huanghuanlai/dounine/github/testdir/interfaceapidoc");
        try {
            ApplicationServer.serverStart(8989,filePath.getServerIndexHtmlPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
