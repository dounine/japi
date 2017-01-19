package com.dounine.japi.core.impl;

import com.dounine.japi.core.IAction;
import com.dounine.japi.core.IDoc;
import com.dounine.japi.core.IMethod;
import com.dounine.japi.core.type.DocType;
import com.dounine.japi.entity.User;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
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

    private static final String[] MATCH_CHARTS = {"public ", "class ", "interface ", "@interface ", "enum ", "abstract ", "@interface "};

    private String javaFilePath;
    /**
     * 注释单个值
     */
    private static final String[] SINGLE_DOC_VALUE = {"return"};
    /**
     * 注释名称[  * @return]
     */
    private static final Pattern DOC_NAME = Pattern.compile("[*]\\s[@]\\S*");
    /**
     * 注解
     */
    private static final Pattern ANNOTATION_PATTERN = Pattern.compile("^\\s*[@][\\S]*");
    /**
     * 注释名称跟值[  * @param name]
     */
    private static final Pattern DOC_NAME_VALUE = Pattern.compile("[*]\\s[@]\\S*\\s\\S*");
    /**
     * 注释头[  * (这是注释)]
     */
    private static final Pattern DOC_MORE = Pattern.compile("^(\\s*)[*]\\s");
    /**
     * 方法说明注释
     */
    private static final Pattern DOC_METHOD_FUN_DES = Pattern.compile("^(\\s*)[*]\\s[^@]\\S*");
    /**
     * 注释开始
     */
    private static final Pattern DOC_PATTERN_BEGIN = Pattern.compile("^(\\s*)[/][*]{2}$");
    /**
     * 注释结束
     */
    private static final Pattern DOC_PATTERN_END = Pattern.compile("^(\\s*)[*][/]$");
    /**
     * 方法头
     */
    private static final Pattern[] METHOD_KEYWORD = {Pattern.compile("^(\\s*)(public)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(private)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(void)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$"), Pattern.compile("^(\\s*)(protected)(\\s*)(\\S*)(\\s*)([a-zA-Z0-9]*)(\\s*)[(].*[{]$")};
    /**
     * 方法头返回值部分
     */
    private static final Pattern[] METHOD_RETURN_TYPES = {Pattern.compile("^(\\s*)(public)\\s*\\S*\\s*[a-zA-z0-9]*"), Pattern.compile("^(\\s*)(private)\\s*\\S*\\s*[a-zA-z0-9]*"), Pattern.compile("^(\\s*)(void)\\s*\\S*\\s*[a-zA-z0-9]*"), Pattern.compile("^(\\s*)(protected)\\s*\\S*\\s*[a-zA-z0-9]*")};

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
            List<String> noPackageLines = new ArrayList<>();
            boolean match = false;//true 找到类的开始，开始查找方法
            for (String line : javaFileLines) {
                if (!match) {
                    for (String chart : MATCH_CHARTS) {
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
            throw new JapiException(e.getMessage());
        }
        return methods;
    }

    /**
     * @param a
     * @param b
     * @param c
     * @param pp
     * @return
     */
    public String mm(@Validated StringUtils a, String b, int c, double pp) {
        return "";
    }

    /**
     * 提供类的方法信息
     *
     * @param noPackageLines 不包含package头部与类尾部行信息
     * @return 方法信息列表
     */
    private List<List<String>> methodBodyAndDoc(List<String> noPackageLines) {
        List<List<String>> methodBodyAndDocs = new ArrayList<>();
        boolean isFindDocBegin = false;
        List<String> methodLines = null;
        Iterator<String> newNoPackageLines = new ArrayList<>(noPackageLines).iterator();
        while (newNoPackageLines.hasNext()) {
            String line = newNoPackageLines.next();
            Matcher docMatcher = DOC_PATTERN_BEGIN.matcher(line);
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

        return methodBodyAndDocs;
    }

    /**
     * 提取方法注释信息
     *
     * @param methodLines
     * @return
     */
    private List<IDoc> extractDoc(List<String> methodLines) {
        boolean methodBegin = false;
        List<IDoc> methodDocs = new ArrayList<>();
        for (String methodLine : methodLines) {
            Matcher matcherDocBegin = DOC_PATTERN_BEGIN.matcher(methodLine);
            if (!methodBegin && matcherDocBegin.find()) {
                methodBegin = true;
                continue;
            }
            if (methodBegin) {
                Matcher matcherDocEnd = DOC_PATTERN_END.matcher(methodLine);
                if (matcherDocEnd.find()) {
                    break;
                }
            }
            if (methodBegin) {
                DocImpl docImpl = new DocImpl();
                Matcher methodFunDesMatcher = DOC_METHOD_FUN_DES.matcher(methodLine);//方法功能描述
                if (methodFunDesMatcher.find()) {
                    Matcher methodMoreMatcher = DOC_MORE.matcher(methodLine);
                    if (methodMoreMatcher.find()) {
                        docImpl.setName(methodFunDesMatcher.group().substring(methodMoreMatcher.group().length()));
                        docImpl.setDocType(DocType.FUNDES);
                    }
                } else {
                    Matcher methodMoreMatcher = DOC_MORE.matcher(methodLine);//注释左   *
                    if (methodMoreMatcher.find()) {//   *
                        docImpl.setName(methodLine.substring(methodMoreMatcher.group().length()));
                        Matcher methodNameMatcher = DOC_NAME.matcher(methodLine);//注释名称 * \@param
                        if (methodNameMatcher.find()) {
                            String methodNameValue = methodNameMatcher.group();
                            String docName = methodNameValue.substring(3);
                            docImpl.setName(docName);
                            Matcher methodNameValueMatcher = DOC_NAME_VALUE.matcher(methodLine);//注释名称 * \@param user
                            int singleDocIndex = Arrays.binarySearch(SINGLE_DOC_VALUE, docName);
                            if (singleDocIndex >= 0) {//是否匹配单个注释：return
                                docImpl.setDocType(DocType.DSINGLE);
                                docImpl.setDes(methodLine.substring(methodLine.indexOf(docName) + SINGLE_DOC_VALUE[singleDocIndex].length()).trim());
                            } else if (methodNameValueMatcher.find()) {
                                String docValue = methodNameValueMatcher.group().substring(methodNameValue.length());
                                String docDes = methodLine.substring(methodLine.indexOf(docValue)).trim().substring(docValue.length());
                                docImpl.setValue(docValue);
                                docImpl.setDes(docDes.trim());
                            }
                        }
                    }
                }
                methodDocs.add(docImpl);
            }
        }
        return methodDocs;
    }

    private MethodImpl extractMethod(List<String> methodLines) {
        List<Annotation> annotations = new ArrayList<>();
        List<String> annotationStrs = new ArrayList<>();
        String methodLineStr = null;
        boolean docBegin = false;
        for (String methodLine : methodLines) {
            Matcher matcherDocEnd = DOC_PATTERN_END.matcher(methodLine);
            if (!docBegin && matcherDocEnd.find()) {
                docBegin = true;
                continue;
            }
            if (docBegin) {
                Matcher annotationMatcher = ANNOTATION_PATTERN.matcher(methodLine);
                if(annotationMatcher.find()){//注解
                    annotationStrs.add(methodLine);
                }else{//方法
                    methodLineStr = methodLine;
                }
            }
        }
        String returnTypeStr = null;
        for(Pattern typePattern : METHOD_RETURN_TYPES){
            Matcher returnTypeMatch = typePattern.matcher(methodLineStr);
            if(returnTypeMatch.find()){
                returnTypeStr = returnTypeMatch.group();//public String testUser
                break;
            }
        }
        String returnType = null;
        if(StringUtils.isNotBlank(returnTypeStr)){
            String[] mabyTypes = returnTypeStr.trim().split("\\s");
            if(mabyTypes.length==2){//没有权限修饰符,索引0是返回值类型
                returnType = mabyTypes[0];
            }else if(mabyTypes.length==3){//有权限修饰符,索引1是返回值类型
                returnType = mabyTypes[1];
            }
        }
        System.out.println(returnType);
        return null;
    }

    /**
     * 提取方法信息
     *
     * @return 方法列表
     */
    private IMethod[] extractDocAndMethodInfo(List<List<String>> methodBodyAndDocs) {
        MethodImpl[] methodImpls = new MethodImpl[methodBodyAndDocs.size()];
        for (int i = 0, len = methodBodyAndDocs.size(); i < len; i++) {
            List<String> methodLines = methodBodyAndDocs.get(i);
            MethodImpl methodImpl = new MethodImpl();

            List<IDoc> methodDocs = extractDoc(methodLines);//提取类注释信息
            MethodImpl extractMethod = extractMethod(methodLines);//提取方法信息

            methodImpl.setDocs(methodDocs.toArray(new IDoc[]{}));
            methodImpls[i] = methodImpl;
        }
        System.out.println("========");
        for (MethodImpl method : methodImpls) {
            for (IDoc doc : method.getDocs()) {
                if (doc.getDocType().equals(DocType.FUNDES)) {
                    System.out.println("方法：" + doc.getName());
                } else if (doc.getDocType().equals(DocType.DSINGLE)) {
                    System.out.println("返回值：" + doc.getName() + " : " + doc.getDes());
                } else {
                    System.out.println("参数：" + doc.getValue() + " : " + doc.getDes());
                }
            }
        }
        return methodImpls;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }
}
