package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.Const;
import com.dounine.japi.core.*;
import com.dounine.japi.core.type.DocType;
import com.dounine.japi.entity.User;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class ActionImpl implements IAction {

    private String projectPath;
    private String javaFilePath;
    private List<String> includePaths = new ArrayList<>();

    private static final Logger CONSOLE = LoggerFactory.getLogger(ActionImpl.class);

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
    public List<String> getExcludeTypes() {
        URL url = this.getClass().getResource("/action-builtIn-types.txt");
        File file = new File(url.getFile());
        if (!file.exists()) {
            throw new JapiException(url.getFile() + " 文件不存在");
        }
        try {
            String str = FileUtils.readFileToString(file, Charset.forName("utf-8"));
            str = str.replaceAll("\\s", StringUtils.EMPTY);
            return Arrays.asList(str.split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<IMethod> getMethods() {
        List<IMethod> methods = null;
        try {
            List<String> javaFileLines = FileUtils.readLines(new File(javaFilePath), Charset.forName("utf-8"));
            List<String> noPackageLines = new ArrayList<>();
            boolean match = false;//true 找到类的开始，开始查找方法
            for (String line : javaFileLines) {
                if (!match) {
                    for (String chart : Const.MATCH_CHARTS) {
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
            methods = extractDocAndMethodInfo(methodBodyAndDocs);//提取方法注释及方法信息

        } catch (IOException e) {
            CONSOLE.error(e.getMessage());
            throw new JapiException(e.getMessage());
        }
        return methods;
    }

    /**
     * 提供类的方法信息
     *
     * @param noPackageLines 不包含package头部与类尾部行信息
     * @return 方法信息列表
     */
    private List<List<String>> methodBodyAndDoc(final List<String> noPackageLines) {
        List<List<String>> methodBodyAndDocs = new ArrayList<>();
        boolean isFindDocBegin = false;
        List<String> methodLines = null;
        Iterator<String> newNoPackageLines = new ArrayList<>(noPackageLines).iterator();
        while (newNoPackageLines.hasNext()) {
            String line = newNoPackageLines.next();
            Matcher docMatcher = Const.DOC_PATTERN_BEGIN.matcher(line);
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
                for (Pattern methodPattern : Const.METHOD_KEYWORD) {
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

        return methodBodyAndDocs;
    }

    /**
     * 提取方法注释信息
     *
     * @param methodLines
     * @return
     */
    private List<IActionMethodDoc> extractDoc(final List<String> methodLines) {
        boolean methodBegin = false;
        List<IActionMethodDoc> methodDocs = new ArrayList<>();
        for (String methodLine : methodLines) {
            Matcher matcherDocBegin = Const.DOC_PATTERN_BEGIN.matcher(methodLine);
            if (!methodBegin && matcherDocBegin.find()) {
                methodBegin = true;
                continue;
            }
            if (methodBegin) {
                Matcher matcherDocEnd = Const.DOC_PATTERN_END.matcher(methodLine);
                if (matcherDocEnd.find()) {
                    break;
                }
            }
            if (methodBegin) {
                ActionMethodDocImpl docImpl = new ActionMethodDocImpl();
                Matcher methodFunDesMatcher = Const.DOC_METHOD_FUN_DES.matcher(methodLine);//方法功能描述
                if (methodFunDesMatcher.find()) {
                    Matcher methodMoreMatcher = Const.DOC_MORE.matcher(methodLine);
                    if (methodMoreMatcher.find()) {
                        docImpl.setName(methodFunDesMatcher.group().substring(methodMoreMatcher.group().length()));
                        docImpl.setDocType(DocType.FUNDES);
                    }
                } else {
                    Matcher methodMoreMatcher = Const.DOC_MORE.matcher(methodLine);//注释左   *
                    if (methodMoreMatcher.find()) {//   *
                        docImpl.setName(methodLine.substring(methodMoreMatcher.group().length()));
                        Matcher methodNameMatcher = Const.DOC_NAME.matcher(methodLine);//注释名称 * \@param
                        if (methodNameMatcher.find()) {
                            String methodNameValue = methodNameMatcher.group();
                            String docName = methodNameValue.substring(3);
                            docImpl.setName(docName);
                            Matcher methodNameValueMatcher = Const.DOC_NAME_VALUE.matcher(methodLine);//注释名称 * \@param user
                            int singleDocIndex = Arrays.binarySearch(Const.SINGLE_DOC_VALUE, docName);
                            if (singleDocIndex >= 0) {//是否匹配单个注释：return
                                docImpl.setDocType(DocType.DSINGLE);
                                docImpl.setDes(methodLine.substring(methodLine.indexOf(docName) + Const.SINGLE_DOC_VALUE[singleDocIndex].length()).trim());
                            } else if (methodNameValueMatcher.find()) {
                                String docValue = methodNameValueMatcher.group().substring(methodNameValue.length());
                                if (methodLine.endsWith(docValue)) {
                                    CONSOLE.warn(methodLine + " 没有注释");
                                } else {
                                    String docDes = methodLine.substring(methodLine.indexOf(docValue)).trim().substring(docValue.length());
                                    docImpl.setDes(docDes.trim());
                                }
                                docImpl.setValue(docValue);
                            }
                        }
                    }
                }
                if (StringUtils.isBlank(docImpl.getName()) && StringUtils.isBlank(docImpl.getValue()) && StringUtils.isBlank(docImpl.getDes())) {
                    continue;//去掉无效的换行注释
                }
                methodDocs.add(docImpl);
            }
        }
        return methodDocs;
    }


    private IReturnType getMethodReturnType(final String returnTypeStr) {
        ReturnTypeImpl returnTypeImpl =  new ReturnTypeImpl();
        returnTypeImpl.setJavaFilePath(javaFilePath);
        returnTypeImpl.setProjectPath(projectPath);
        returnTypeImpl.getIncludePaths().addAll(includePaths);
        returnTypeImpl.setJavaKeyTxt(returnTypeStr);
        return returnTypeImpl;
    }

    /**
     * 获取方法返回值
     * @param methodLineStr 方法行
     * @return 类型：String
     */
    private String getMethodReturnTypeStr(final String methodLineStr) {
        String returnTypeStr = null;
        for (Pattern typePattern : Const.METHOD_RETURN_TYPES) {
            Matcher returnTypeMatch = typePattern.matcher(methodLineStr);
            if (returnTypeMatch.find()) {
                returnTypeStr = StringUtils.substring(returnTypeMatch.group(), 0, -1).trim();//public String testUser
                break;
            }
        }
        String returnType = null;
        if (StringUtils.isNotBlank(returnTypeStr)) {
            returnType = returnTypeStr.substring(returnTypeStr.indexOf(StringUtils.SPACE), returnTypeStr.lastIndexOf(StringUtils.SPACE));
            returnType = returnType.trim();
        }
        return returnType;
    }

    /**
     * 获取方法参数信息
     *
     * @param methodLineStr 方法行
     * @return 参数列表
     */
    private List<String> getMethodParameters(String methodLineStr) {
        List<String> parameters = new ArrayList<>();
        Matcher matcher = Const.PARAMETER_BODYS.matcher(methodLineStr);
        if (matcher.find()) {
            String parStrs = StringUtils.substring(matcher.group(), 1, -1).trim();
            parStrs = StringUtils.substring(parStrs.trim(), 0, -1);
            Matcher parMatcher = Const.PARAMETER_SINGLE_NAME.matcher(parStrs);
            int initSearchIndex = 0;
            int lastSearchIndex = 0;
            while (parMatcher.find()) {
                lastSearchIndex = parMatcher.end();
                parameters.add(parStrs.substring(initSearchIndex, lastSearchIndex - 1).trim());
                initSearchIndex = lastSearchIndex;
            }
            if (lastSearchIndex < parStrs.length()) {
                parameters.add(parStrs.substring(lastSearchIndex));
            }
        }
        return parameters;
    }

    /**
     * 提取方法信息
     *
     * @param methodLines 方法doc及注解及行信息
     * @return 方法信息
     */
    private MethodImpl extractMethod(List<String> methodLines) {
        MethodImpl methodImpl = new MethodImpl();
        List<String> annotationStrs = new ArrayList<>();
        String methodLineStr = null;
        boolean docBegin = false;
        for (String methodLine : methodLines) {
            Matcher matcherDocEnd = Const.DOC_PATTERN_END.matcher(methodLine);
            if (!docBegin && matcherDocEnd.find()) {
                docBegin = true;
                continue;
            }
            if (docBegin) {
                Matcher annotationMatcher = Const.ANNOTATION_PATTERN.matcher(methodLine);
                if (annotationMatcher.find()) {//注解
                    annotationStrs.add(methodLine.trim().substring(1));
                } else {//方法
                    methodLineStr = methodLine.trim();
                }
            }
        }
        String returnTypeStr = getMethodReturnTypeStr(methodLineStr);
        IReturnType returnType = getMethodReturnType(returnTypeStr);
        returnType.getFields();
//        System.out.println(JSON.toJSONString(returnType.getFields()));

        List<String> methodParameters = getMethodParameters(methodLineStr);

        methodImpl.setReturnType(returnTypeStr);
        methodImpl.setAnnotations(annotationStrs);
        methodImpl.setParameters(methodParameters);

        return methodImpl;
    }

    /**
     * 提取方法信息
     *
     * @return 方法列表
     */
    private List<IMethod> extractDocAndMethodInfo(final List<List<String>> methodBodyAndDocs) {
        List<IMethod> methodImpls = new ArrayList<>(methodBodyAndDocs.size());
        for (List<String> methodLines : methodBodyAndDocs) {
            MethodImpl methodImpl = new MethodImpl();

            List<IActionMethodDoc> methodDocs = extractDoc(methodLines);//提取类注释信息
            MethodImpl extractMethod = extractMethod(methodLines);//提取方法信息

            methodImpl.setDocs(methodDocs);
            methodImpl.setAnnotations(extractMethod.getAnnotations());
            methodImpl.setReturnType(extractMethod.getReturnType());
            methodImpl.setParameters(extractMethod.getParameters());

            methodImpls.add(methodImpl);
        }
        System.out.println("========");
        JavaFileImpl javaFile = new JavaFileImpl();
        javaFile.setJavaFilePath(javaFilePath);
        javaFile.setProjectPath(projectPath);
        javaFile.getIncludePaths().addAll(includePaths);
        for (IMethod iMethod : methodImpls) {
            System.out.println("返回类型：" + iMethod.getReturnType());
            File file = javaFile.searchTxtJavaFileForProjectsPath(iMethod.getReturnType());
            System.out.println("类型文件：" + file.getAbsolutePath());
            System.out.println("参数类型：" + JSON.toJSONString(iMethod.getParameters()));
            System.out.println("----------");
            for (IActionMethodDoc doc : iMethod.getDocs()) {
                if (doc.getDocType().equals(DocType.FUNDES)) {
                    System.out.println("方法：" + doc.getName());
                } else if (doc.getDocType().equals(DocType.DSINGLE)) {
                    System.out.println("返回值：" + doc.getName() + " : " + doc.getDes());
                } else {
                    System.out.println("参数：" + doc.getValue() + " : " + doc.getDes());
                }
            }
            System.out.println("----------");
        }
        return methodImpls;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }
}
