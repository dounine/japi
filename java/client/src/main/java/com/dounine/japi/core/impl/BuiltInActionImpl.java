package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
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
public class BuiltInActionImpl implements IBuiltIn {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuiltInActionImpl.class);

    private List<String> types;
    private static final BuiltInActionImpl builtIn = new BuiltInActionImpl();

    static {
        List<String> _types = new ArrayList<>();
        _types.add("org.springframework.validation.BindingResult");
        _types.add("javax.servlet.http.HttpServletRequest");
        _types.add("javax.servlet.http.HttpServletResponse");
        _types.add("org.springframework.web.multipart.MultipartFile");
        if(null==builtIn.types){
            builtIn.types = _types;
        }else{
            builtIn.types.removeAll(_types);
            builtIn.types.addAll(_types);
        }
    }

    private BuiltInActionImpl() {
        URL url = JapiClient.class.getResource("/action-builtIn-types.txt");
        File builtInFile = null;
        if (null != url) {
            builtInFile = new File(url.getFile());
        } else {
            url = JapiClient.class.getResource("/japi/action-builtIn-types.txt");
            if (null != url) {
                builtInFile = new File(url.getFile());
            }
        }
        if (url == null) {
//            LOGGER.warn("action-builtIn-types.txt 文件不存在,使用默认参数.");
        } else {
            String builtInFilePath = builtInFile.getAbsolutePath();
            try {
                String typesStr = FileUtils.readFileToString(new File(builtInFilePath), Charset.forName("utf-8"));
                typesStr = typesStr.replaceAll("\\s", "");//去掉回车
                types = new ArrayList<>(Arrays.asList(typesStr.split(",")));
            } catch (IOException e) {
                throw new JapiException(e.getMessage());
            }
        }
    }

    public static BuiltInActionImpl getInstance() {
        return builtIn;
    }

    private List<String> getBuiltInTypes() {
        return types;
    }

    @Override
    public boolean isBuiltInType(String keyType) {
        List<String> keyTypes = getBuiltInTypes();
        boolean isBuiltIn = false;
        for (String key : keyTypes) {
            if (key.equals(keyType) || key.endsWith(keyType)) {
                isBuiltIn = true;
                break;
            }
        }
        return isBuiltIn;
    }
}
