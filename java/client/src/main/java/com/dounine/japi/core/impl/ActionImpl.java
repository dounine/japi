package com.dounine.japi.core.impl;

import com.dounine.japi.core.IAction;
import com.dounine.japi.core.IMethod;
import com.dounine.japi.entity.User;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class ActionImpl implements IAction {

    private String javaFilePath;
    private static final Pattern[] METHOD_KEYWORD = {Pattern.compile("^(\\s*)(public)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(private)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(void)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(protected)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$")};

    @Override
    public String readClassInfo() {
        return null;
    }

    public String readClassInfo(@Validated(value = {}) User user) {
        return null;
    }

    @Override
    public String readPackageName() {
        return null;
    }

    @Override
    public IMethod[] getMethods() {
        IMethod[] methods = null;
        File file = new File(javaFilePath);
        try {
            List<String> javaFileLines = FileUtils.readLines(file, Charset.forName("utf-8"));
            String[] matchCharts = {"public ", "class ", "interface ", "@interface ", "enum ", "abstract ", "@interface "};
            List<String> noPackageLines = new ArrayList<>();
            boolean match = false;//true 找到类的开始，开始查找方法
            for (String line : javaFileLines) {
                if (!match) {
                    for (String chart : matchCharts) {
                        if (line.startsWith(chart)) {
                            match = true;
                            break;
                        }
                    }
                }
                if (match) {
                    noPackageLines.add(line);
                }
            }
            noPackageLines = noPackageLines.subList(1, noPackageLines.size() - 1);//去掉类头与尾巴
            List<List<String>> methodBodyAndDocs = methodBodyAndDoc(noPackageLines);
            methods = extractMethodInfo(methodBodyAndDocs);

        } catch (IOException e) {
            throw new JapiException(e.getMessage());
        }
        return methods;
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^(\\s*)(public)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$");
        Matcher matcher = pattern.matcher("    public String aa(User user) {");
        if (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    /**
     * 提供类的方法信息
     *
     * @param noPackageLines 不包含package头部与类尾部行信息
     * @return 方法信息列表
     */
    private List<List<String>> methodBodyAndDoc(List<String> noPackageLines) {
        List<List<String>> methodBodyAndDocs = new ArrayList<>();
        Pattern docPattern = Pattern.compile("^(\\s*)[/][*]{2}$");
        boolean isFindDocBegin = false;
        List<String> methodLines = null;
        Iterator<String> newNoPackageLines = new ArrayList<>(noPackageLines).iterator();
        while (newNoPackageLines.hasNext()) {
            String line = newNoPackageLines.next();
            Matcher docMatcher = docPattern.matcher(line);
            if (!isFindDocBegin && docMatcher.find()) {//匹配到注释
                isFindDocBegin = true;
            }
            if (isFindDocBegin) {
                if (null == methodLines) {
                    methodLines = new ArrayList<>();
                    methodLines.add(line);
                    newNoPackageLines.remove();
                } else if (null != methodLines && methodLines.size() > 0) {
                    methodLines.add(line);
                    newNoPackageLines.remove();
                }
            }
            if (null != methodLines && methodLines.size() > 0) {
                for (Pattern methodPattern : METHOD_KEYWORD) {
                    Matcher matcher = methodPattern.matcher(line);
                    if (matcher.find()) {//匹配到方法
                        methodBodyAndDocs.add(methodLines);
                        methodLines = null;
                        isFindDocBegin = false;
                        break;
                    }
                }
            }
        }
        for (List<String> methodDocLines : methodBodyAndDocs) {
            for (String methodLine : methodDocLines) {
                System.out.println(methodLine);
            }
        }
        return null;
    }

    /**
     * 提取方法信息
     *
     * @return 方法列表
     */
    private IMethod[] extractMethodInfo(List<List<String>> methodBodyAndDocs) {

        return null;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }
}
