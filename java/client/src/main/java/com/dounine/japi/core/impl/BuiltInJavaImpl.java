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

    private List<String> types;
    private static final BuiltInJavaImpl builtIn = new BuiltInJavaImpl();

    static {
        List<String> _types = new ArrayList<>();
        _types.add("int");
        _types.add("Integer");
        _types.add("String");
        _types.add("long");
        _types.add("Long");
        _types.add("float");
        _types.add("Float");
        _types.add("double");
        _types.add("Double");
        _types.add("char");
        _types.add("byte");
        _types.add("Byte");
        _types.add("Object");
        _types.add("LocalDateTime");
        _types.add("LocalDate");
        _types.add("LocaTime");
        _types.add("short");

        if(null==builtIn.types){
            builtIn.types = _types;
        }else{
            builtIn.types.removeAll(_types);
            builtIn.types.addAll(_types);
        }
    }

    private BuiltInJavaImpl() {
        URL url = JapiClient.class.getResource("/class-builtIn-types.txt");
        File builtInFile = null;
        if (null != url) {
            builtInFile = new File(url.getFile());
        } else {
            url = JapiClient.class.getResource("/japi/class-builtIn-types.txt");
            if (null != url) {
                builtInFile = new File(url.getFile());
            }
        }
        if (null == url) {
//            LOGGER.warn("class-builtIn-types.txt 文件不存在,使用默认参数.");
        } else {
            String builtInPath = builtInFile.getAbsolutePath();
            try {
                String typesStr = FileUtils.readFileToString(new File(builtInPath), Charset.forName("utf-8"));
                typesStr = typesStr.replaceAll("\\s", "");//去掉回车
                types = new ArrayList<>(Arrays.asList(typesStr.split(",")));
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
