package com.dounine.japi.core.impl;

import com.dounine.japi.core.IBuiltIn;
import com.dounine.japi.core.MainRoot;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

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
public class BuiltInImpl implements IBuiltIn {
    private String[] builtInPaths;
    private File builtInFile;

    public BuiltInImpl(){
        URL url = this.getClass().getResource("/built-in.txt");
        builtInFile = new File(url.getFile());
        if(!builtInFile.exists()){
            throw new JapiException(url.getFile()+" 文件不存在");
        }
        builtInPaths = new String[]{url.getFile()};
    }

    @Override
    public List<String> getBuiltInTypes() {
        if (null == builtInPaths && builtInPaths.length == 0) {
            throw new JapiException("builtInPaths 不能为空,至少有一个文件");
        }
        List<String> types = new ArrayList<>();
        try {
            String typesStr = FileUtils.readFileToString(builtInFile, Charset.forName("utf-8"));
            typesStr = typesStr.replaceAll("\\s","");//去掉回车
            types.addAll(Arrays.asList(typesStr.split(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return types;
    }

    public void setBuiltInPaths(String[] builtInPaths) {
        this.builtInPaths = builtInPaths;
    }
}
