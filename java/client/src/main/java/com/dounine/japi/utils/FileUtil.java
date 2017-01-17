package com.dounine.japi.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by ike on 17-1-17.
 */
public class FileUtil {

    public static HttpPost UPLOAD_REQUEST = new HttpPost("http://localhost:8080/interfaceapidoc/upload");

    public static String upload(String projectName,String filePathParameter,File uploadFile) {

        return upload(projectName,filePathParameter,uploadFile,false);
    }

    public static String upload(String projectName,String filePathParameter,File uploadFile,boolean returnResult) {

        final MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        multipartEntity.setCharset(Charset.forName("utf-8"));
        multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("projectName", new StringBody(projectName, ContentType.APPLICATION_JSON));
        multipartEntity.addPart("filePathParameter", new StringBody(filePathParameter, ContentType.APPLICATION_JSON));
        multipartEntity.addBinaryBody("file", uploadFile);
        UPLOAD_REQUEST.setEntity(multipartEntity.build());

        HttpClient httpClient = HttpClients.createMinimal();
        try {
            HttpResponse httpResponse = httpClient.execute(UPLOAD_REQUEST);
            if(returnResult){
                return EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
