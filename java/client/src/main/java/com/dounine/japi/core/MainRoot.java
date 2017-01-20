package com.dounine.japi.core;

import com.dounine.japi.core.impl.ActionImpl;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.entity.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("main")
public class MainRoot {
//@Validated(value = {IMethod.class, IParameterField.class}) User user, String bb, Integer[] last

    /**
     * 测试例子
     * @param user 用户信息
     * @param bb 测试参数
     * @param last 测试参数1
     * @return {"success":"成功" | "error":"错误"}
     */
    @GetMapping("aa")
    @ResponseBody
    public User testUser(@Validated(value = {IMethod.class, IParameterField.class}) User user, String bb, Integer[] last) {
        return null;
    }

    public static String javaFilePath = "/Users/huanghuanlai/dounine/github/japi/java/client/src/main/java/com/dounine/japi/core/MainRoot.java";
    public static String[] includePaths = {"/Users/huanghuanlai/dounine/github/japi/java/api/src/main/java"};
    public static String projectPath = "/Users/huanghuanlai/dounine/github/japi/java/client/src/main/java";
    public static String buildInPath = "/Users/huanghuanlai/dounine/github/japi/java/client/src/main/resources/class-builtIn-types.txt";

    public static void main(String[] args) {
        JavaFileImpl javaFile = new JavaFileImpl();
        javaFile.setJavaFilePath(javaFilePath);
        javaFile.setProjectPath(projectPath);
        javaFile.getIncludePaths().addAll(Arrays.asList(includePaths));
//        File file = javaFile.searchTxtJavaFileForProjectsPath("com.dounine.japi.entity.User");

        ActionImpl actionImpl = new ActionImpl();
        actionImpl.setJavaFilePath(javaFilePath);
        actionImpl.setProjectPath(projectPath);
        actionImpl.getIncludePaths().addAll(Arrays.asList(includePaths));
        List<IMethod> methods = actionImpl.getMethods();

//        BuiltInImpl builtIn = new BuiltInImpl();

    }

}