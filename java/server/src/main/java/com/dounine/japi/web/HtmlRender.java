package com.dounine.japi.web;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by ike on 17-1-15.
 */
@Component
public class HtmlRender implements Render{

    @Value("${htmlRootPath}")
    private String htmlRootPath;

    public void show(String path){
        String txt = null;
        try {
            txt = FileUtils.readFileToString(new File(htmlRootPath+"/"+path+".html"),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpServletResponse response = ResponseContext.get();
            response.setHeader("Content-Type","text/html; charset=UTF-8");
            response.getWriter().print(txt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
