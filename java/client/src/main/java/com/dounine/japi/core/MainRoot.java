package com.dounine.japi.core;

import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.entity.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class MainRoot {


    private static String java_path = "/Users/huanghuanlai/dounine/github/japi/java/client/src/main/java/com/dounine/japi/core/MainRoot.java";
    public static String[] rootPaths = {"/Users/huanghuanlai/dounine/github/japi/java/client/src/main/java", "/Users/huanghuanlai/dounine/github/japi/java/api/src/main/java"};

    public static void main(String[] args) {
        JavaFileImpl javaFile = new JavaFileImpl();
        javaFile.setJavaFilePath(java_path);
        javaFile.setProjectsPath(rootPaths);
        File file = javaFile.searchTxtJavaFileForProjectsPath("User");
        System.out.println(file.getAbsolutePath());
    }

}