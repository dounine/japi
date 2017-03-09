package com.dounine.japi.core;

import com.dounine.japi.core.impl.SearchInfo;

import java.io.File;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public interface IJavaFile {

    /**
     * 检测java内容的文件是否存在
     * @param javaTxt java内容关键字
     * @return 找到的文件
     */
    SearchInfo searchTxtJavaFileForProjectsPath(String javaTxt, String javaFilePath);

}
