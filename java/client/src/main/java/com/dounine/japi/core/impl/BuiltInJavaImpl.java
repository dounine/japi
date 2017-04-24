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
public class BuiltInJavaImpl implements IBuiltIn {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuiltInJavaImpl.class);

    private List<String> types = new ArrayList<>();
    private static final BuiltInJavaImpl builtIn = new BuiltInJavaImpl();

    static {
        builtIn.types.add("int");
        builtIn.types.add("Integer");
        builtIn.types.add("String");
        builtIn.types.add("Boolean");
        builtIn.types.add("boolean");
        builtIn.types.add("long");
        builtIn.types.add("Long");
        builtIn.types.add("float");
        builtIn.types.add("Float");
        builtIn.types.add("double");
        builtIn.types.add("Double");
        builtIn.types.add("char");
        builtIn.types.add("byte");
        builtIn.types.add("Byte");
        builtIn.types.add("Object");
        builtIn.types.add("LocalDateTime");
        builtIn.types.add("LocalDate");
        builtIn.types.add("LocaTime");
        builtIn.types.add("short");

    }

    private BuiltInJavaImpl() {
        URL url = null;
        if(null!=JapiClient.getClassLoader()){
            url = JapiClient.getClassLoader().getResource("/class-builtIn-types.txt");
            if(!new File(url.getFile()).exists()){
                url = JapiClient.getClassLoader().getResource("/japi/class-builtIn-types.txt");
            }
        }

        File builtInFile = null;
        if (null != url) {
            builtInFile = new File(url.getFile());
        }
        if (null == url) {
//            LOGGER.warn("class-builtIn-types.txt 文件不存在,使用默认参数.");
        } else {
            String builtInPath = builtInFile.getAbsolutePath();
            try {
                File file = new File(builtInPath);
                if(file.exists()){
                    String typesStr = FileUtils.readFileToString(file, Charset.forName("utf-8"));
                    typesStr = typesStr.replaceAll("\\s", "");//去掉回车
                    types.addAll(Arrays.asList(typesStr.split(",")));
                }

            } catch (IOException e) {
                throw new JapiException(e.getMessage());
            }
        }

    }

    public static BuiltInJavaImpl getInstance() {
        return builtIn;
    }

    private List<String> getBuiltInTypes() {
        return types;
    }

    public boolean isBuiltInType(String keyType) {
        List<String> keyTypes = getBuiltInTypes();
        boolean isBuiltIn = false;
        for (String key : keyTypes) {
            if (key.equals(keyType) || key.endsWith(keyType) || keyType.equals(key + "[]") || (key + "[]").endsWith(keyType)) {
                isBuiltIn = true;
                break;
            }
        }
        return isBuiltIn;
    }
}
