package com.dounine.japi.core.impl;

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
public class BuiltInActionImpl implements IBuiltIn {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuiltInActionImpl.class);

    private static String[] builtInPaths;
    private static List<String> types;
    private static File builtInFile;
    private static final BuiltInActionImpl builtIn = new BuiltInActionImpl();

    private BuiltInActionImpl() {
        if (null == builtInFile) {
            URL url = this.getClass().getResource("/action-builtIn-types.txt");
            builtInFile = new File(url.getFile());
            if (!builtInFile.exists()) {
                String err = url.getFile() + " 文件不存在";
                LOGGER.error(err);
                throw new JapiException(err);
            }
            builtInPaths = new String[]{url.getFile()};
        }
    }

    public static BuiltInActionImpl getInstance(){
        return builtIn;
    }

    @Override
    public List<String> getBuiltInTypes() {
        if (null == builtInPaths || (null != builtInPaths && builtInPaths.length == 0)) {
            String err = "builtInPaths 不能为空,至少有一个文件";
            LOGGER.error(err);
            throw new JapiException(err);
        }
        if (null != types) {
            return types;
        }
        try {
            types = new ArrayList<>();
            String typesStr = FileUtils.readFileToString(builtInFile, Charset.forName("utf-8"));
            typesStr = typesStr.replaceAll("\\s", "");//去掉回车
            types.addAll(Arrays.asList(typesStr.split(",")));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return types;
    }

    public boolean isBuiltInType(String keyType) {
        List<String> keyTypes = getBuiltInTypes();
        boolean isBuiltIn = false;
        for(String key : keyTypes){
            if(key.equals(keyType)||key.endsWith(keyType)){
                isBuiltIn = true;
                break;
            }
        }
        return isBuiltIn;
    }

    public void setBuiltInPaths(String[] builtInPaths) {
        this.builtInPaths = builtInPaths;
    }
}
