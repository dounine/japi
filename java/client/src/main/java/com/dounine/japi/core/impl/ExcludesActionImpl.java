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
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public class ExcludesActionImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcludesActionImpl.class);

    private static String[] excludesPaths;
    private static List<String> types;
    private static File excludesFile;
    private static final ExcludesActionImpl builtIn = new ExcludesActionImpl();

    private ExcludesActionImpl() {
        if (null == excludesFile) {
            URL url = JapiClient.class.getResource("/action-excludes-tags.txt");
            excludesFile = new File(url.getFile());
            if (!excludesFile.exists()) {
                url = JapiClient.class.getResource("/japi/action-excludes-tags.txt");
                excludesFile = new File(url.getFile());
            }
            if(!excludesFile.exists()){
                String err = url.getFile() + " 文件不存在";
                LOGGER.error(err);
                throw new JapiException(err);
            }
            excludesPaths = new String[]{url.getFile()};
        }
    }

    public static ExcludesActionImpl getInstance(){
        return builtIn;
    }

    public List<String> getExcludesTags() {
        if (null == excludesPaths || (null != excludesPaths && excludesPaths.length == 0)) {
            String err = "excludesPaths 不能为空,至少有一个文件";
            LOGGER.error(err);
            throw new JapiException(err);
        }
        if (null != types) {
            return types;
        }
        try {
            types = new ArrayList<>();
            types = FileUtils.readLines(excludesFile, Charset.forName("utf-8"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return types;
    }

    public boolean isExcludesTag(String keyTag) {
        List<String> keyTypes = getExcludesTags();
        boolean isBuiltIn = false;
        for(String key : keyTypes){
            if(key.equals(keyTag)||key.endsWith(keyTag)){
                isBuiltIn = true;
                break;
            }
        }
        return isBuiltIn;
    }

    public void setExcludesPaths(String[] excludesPaths) {
        this.excludesPaths = excludesPaths;
    }
}
