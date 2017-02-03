package com.dounine.japi.test.test;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class MainTest {

    private String actionPath;

    @Before
    public void getActionPath(){
        actionPath = "/Users/huanghuanlai/dounine/github/japi/java/client/src/test/client/com/dounine/japi/test/web";
    }

    @Test
    public void testAction() throws IOException {
        String jarPath = MainTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        URL url=new URL("jar:file:" + jarPath + "!/Test.java");
        InputStream is=url.openStream();
        byte b[]=new byte[1000];
        is.read(b);
        System.out.println(new String(b).trim());
        console(url);
//        URL url = new URL("jar:file:")
//        try {
//           InputStream is = MainTest.class.getClassLoader().getResourceAsStream("/src/test/com/dounine/japi/test/entity/UserChild.java");
//        try {
//            console(is.available());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//            Class.forName("com.dounine.japi.test.entity.UserChild");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public void console(Object val) {
        System.out.println(val);
    }
}
