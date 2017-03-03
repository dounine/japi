package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.JapiClient;
import com.dounine.japi.core.IDocTag;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by lake on 17-2-6.
 */
public class DocTagImpl implements IDocTag {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocTagImpl.class);

    private static String[] builtInPaths;
    private static Map<String,String> types;
    private static File docTagFile;
    private static final DocTagImpl DOC_TAG = new DocTagImpl();

    public static DocTagImpl getInstance(){
        return DOC_TAG;
    }

    private DocTagImpl() {
        if (null == docTagFile) {
            URL url = JapiClient.class.getResource("/doc-tags.txt");
            docTagFile = new File(url.getFile());
            if (!docTagFile.exists()) {
                url = JapiClient.class.getResource("/japi/doc-tags.txt");
                docTagFile = new File(url.getFile());
            }
            if(!docTagFile.exists()){
                String err = url.getFile() + " 文件不存在";
                LOGGER.error(err);
                throw new JapiException(err);
            }
            builtInPaths = new String[]{url.getFile()};
        }
    }

    private Map<String,String> getDocTags() {
        if (null != types) {
            return types;
        }
        try {
            List<String> tagDocs = FileUtils.readLines(docTagFile,Charset.forName("utf-8"));
            types = new HashMap<>(tagDocs.size());
            tagDocs.forEach(td->{
                String[] tds = td.split(StringUtils.SPACE);
                types.put(tds[0],tds[1]);
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return types;
    }

    public void setBuiltInPaths(String[] builtInPaths) {
        this.builtInPaths = builtInPaths;
    }

    @Override
    public String getTagDesByName(String tagName) {
        return getDocTags().get(tagName);
    }
}
