package com.dounine.japi.core;

import com.dounine.japi.core.impl.ActionImpl;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.entity.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("main")
public class MainRoot {
//@Validated(value = {IActionMethod.class, IParameterField.class}) UserChild user, String bb, Integer[] last

    /**
     * 测试例子
     * @param user 用户信息
     * @param bb  测试参数
     * @param last 测试参数1
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "aa")
    @ResponseBody
    public User testUser(@Validated(value = {IActionMethod.class, IParameterField.class}) User user,@RequestParam Integer cc, BindingResult bindingResult) {
        return null;
    }

    /**
     * 哈哈
     *
     * @param user 没用户
     * @param bb 测试参数
     * @param last
     * @return class User
     * @deprecated1 yes
     * @deprecated yes
     */
    @RequestMapping(value = "login",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public void login(@Validated(value = {IActionMethod.class, IParameterField.class}) User user, String bb, Integer[] last) {
    }

    public static String javaFilePath = "/home/lake/github/japi/java/client/src/main/java/com/dounine/japi/core/MainRoot.java";
    public static String[] includePaths = {"/home/lake/github/japi/java/api/src/main/java"};
    public static String projectPath = "/home/lake/github/japi/java/client/src/main/java";
    public static String buildInPath = "/home/lake/github/japi/java/client/src/main/resources/class-builtIn-types.txt";

    public static void main(String[] args) {
        JavaFileImpl javaFile = new JavaFileImpl();
        javaFile.setJavaFilePath(javaFilePath);
        javaFile.setProjectPath(projectPath);
        javaFile.getIncludePaths().addAll(Arrays.asList(includePaths));
//        File file = javaFile.searchTxtJavaFileForProjectsPath("com.dounine.japi.entity.UserChild");

        ActionImpl actionImpl = new ActionImpl();
        actionImpl.setJavaFilePath(javaFilePath);
        actionImpl.setProjectPath(projectPath);
        actionImpl.getIncludePaths().addAll(Arrays.asList(includePaths));
        List<IActionMethod> methods = actionImpl.getMethods();

//        BuiltInImpl builtIn = new BuiltInImpl();

    }

}