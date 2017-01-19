package com.dounine.japi.core;

import com.dounine.japi.core.impl.ActionImpl;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.entity.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("main")
public class MainRoot {

    /**
     * 测试例子
     * @param user 用户信息
     * @return {"success":"成功" | "error":"错误"}
     */
    @GetMapping("aa")
    public String testUser(User user){
        return "success";
    }


    @GetMapping("aa")
    public String dd(User user){
        return "success";
    }


    public static String javaFilePath = "/Users/huanghuanlai/dounine/github/japi/java/client/src/main/java/com/dounine/japi/core/MainRoot.java";
    public static String[] projectsPaths = {"/Users/huanghuanlai/dounine/github/japi/java/api/src/main/java"};
    public static String projectPath = "/Users/huanghuanlai/dounine/github/japi/java/client/src/main/java";

    public static void main(String[] args) {
        JavaFileImpl javaFile = new JavaFileImpl();
        javaFile.setJavaFilePath(javaFilePath);
        javaFile.setProjectPath(projectPath);
        javaFile.getIncludePaths().addAll(Arrays.asList(projectsPaths));
        File file = javaFile.searchTxtJavaFileForProjectsPath("User");

        ActionImpl actionImpl = new ActionImpl();
        actionImpl.setJavaFilePath(javaFilePath);
        IMethod[] methods = actionImpl.getMethods();
        //System.out.println(methods.length);
    }

}