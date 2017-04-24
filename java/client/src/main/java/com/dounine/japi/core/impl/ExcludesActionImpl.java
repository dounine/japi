package com.dounine.japi.core.impl;

import com.dounine.japi.JapiClient;
import com.dounine.japi.core.IBuiltIn;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public class ExcludesActionImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcludesActionImpl.class);

    private List<String> types = new ArrayList<>();
    private static final ExcludesActionImpl builtIn = new ExcludesActionImpl();

    static {
        builtIn.types.add("throws");
        builtIn.types.add("version");
        builtIn.types.add("return");
        builtIn.types.add("param");
    }

    private ExcludesActionImpl() {
        URL url = null;
        if(null!=JapiClient.getClassLoader()){
            url = JapiClient.getClassLoader().getResource("/action-excludes-tags.txt");
            if(!new File(url.getFile()).exists()){
                url = JapiClient.getClassLoader().getResource("/japi/action-excludes-tags.txt");
            }
        }

        File excludesFile = null;
        if (null != url) {
            excludesFile = new File(url.getFile());
        }
        if (url == null) {
//                LOGGER.warn("action-excludes-tags 文件不存在,使用默认参数.");
        } else {
            String excludesPath = excludesFile.getAbsolutePath();
            try {
                File file= new File(excludesPath);
                if (file.exists()){
                    types.addAll(FileUtils.readLines(file, Charset.forName("utf-8")));
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public static ExcludesActionImpl getInstance() {
        return builtIn;
    }

    private List<String> getExcludesTags() {
        return types;
    }

    public boolean isExcludesTag(String keyTag) {
        List<String> keyTypes = getExcludesTags();
        boolean isBuiltIn = false;
        for (String key : keyTypes) {
            if (key.equals(keyTag) || key.endsWith(keyTag)) {
                isBuiltIn = true;
                break;
            }
        }
        return isBuiltIn;
    }

}
