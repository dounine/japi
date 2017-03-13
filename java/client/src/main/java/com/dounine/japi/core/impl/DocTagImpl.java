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

    private Map<String, String> types;
    private static final DocTagImpl DOC_TAG = new DocTagImpl();

    static {
        DOC_TAG.types = new HashMap<>();
        DOC_TAG.types.put("return.", "返回值");
        DOC_TAG.types.put("param", "参数");
        DOC_TAG.types.put("deprecated.", "过时");
        DOC_TAG.types.put("des.", "描述");
        DOC_TAG.types.put("version.", "版本");
        DOC_TAG.types.put("stable.", "稳定版本");
    }

    public static DocTagImpl getInstance() {
        return DOC_TAG;
    }

    private DocTagImpl() {
        URL url = JapiClient.class.getResource("/doc-tags.txt");
        File docTagFile = null;
        if (null != url) {
            docTagFile = new File(url.getFile());
        } else {
            url = JapiClient.class.getResource("/japi/doc-tags.txt");
            if (null != url) {
                docTagFile = new File(url.getFile());
            }
        }
        if (null == url) {
//            LOGGER.warn("doc-tags.txt 文件不存在,使用默认参数.");
        } else {
            String builtInPath = docTagFile.getAbsolutePath();
            try {
                File file = new File(builtInPath);
                if(file.exists()){
                    types = new HashMap<>();
                    List<String> tagDocs = FileUtils.readLines(file, Charset.forName("utf-8"));
                    tagDocs.forEach(td -> {
                        String[] tds = td.split(StringUtils.SPACE);
                        types.put(tds[0], tds[1]);
                    });
                }

            } catch (IOException e) {
                throw new JapiException(e.getMessage());
            }
        }
    }

    private Map<String, String> getDocTags() {
        return types;
    }

    @Override
    public String getTagDesByName(String tagName) {
        return getDocTags().get(tagName);
    }
}
