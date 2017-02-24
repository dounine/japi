package com.dounine.japi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lake on 17-2-24.
 */
public class JapiClientTransfer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JapiClientTransfer.class);
    private static final int reties = 3;
    /**
     * seconds
     */
    private static final int tryTime = 3;

    public void autoTransfer(JapiClientStorage japiClientStorage) {
        String serverUrl = japiClientStorage.getProject().getProperties().get("japi.server");
        HttpClient httpClient = HttpClients.createMinimal();
        HttpPost findProject = new HttpPost(serverUrl + "/project");
        List<NameValuePair> datas = new ArrayList<>();
        datas.add(new BasicNameValuePair("name", japiClientStorage.getProject().getProperties().get("japi.name")));
        try {
            findProject.setEntity(new UrlEncodedFormEntity(datas, "utf-8"));
            Integer tryCount = 0;
            while (reties > tryCount) {
                try {
                    HttpResponse response = httpClient.execute(findProject);
                    System.out.println(EntityUtils.toString(response.getEntity()));
                } catch (IOException e) {
                    if (e instanceof HttpHostConnectException) {
                        tryCount++;
                        LOGGER.warn("try connect server "+tryCount+" count.");
                        try {
                            TimeUnit.SECONDS.sleep(tryTime);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            if (tryCount <= reties) {
                LOGGER.error("server connect failed.");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
