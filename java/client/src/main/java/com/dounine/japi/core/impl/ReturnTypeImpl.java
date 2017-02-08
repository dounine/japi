package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.Const;
import com.dounine.japi.core.IBuiltIn;
import com.dounine.japi.core.IReturnField;
import com.dounine.japi.core.IReturnFieldDoc;
import com.dounine.japi.core.IReturnType;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ReturnTypeImpl implements IReturnType {
    private static final Logger CONSOLE = LoggerFactory.getLogger(ReturnTypeImpl.class);

    private String javaFilePath;
    private String projectPath;
    private List<String> includePaths = new ArrayList<>();
    private String javaKeyTxt;
    private List<IReturnField> returnFields;

    @Override
    public List<IReturnField> getFields() {
        if(null==javaKeyTxt){
            CONSOLE.error("javaKeyTxt 不能为空");
            throw new JapiException("javaKeyTxt 不能为空");
        }

        if(BuiltInImpl.getInstance().isBuiltInType(javaKeyTxt)){
            return null;
        }
        if (null == returnFields) {
            returnFields = extractDocAndFieldInfo();//提取属性注释及属性信息
        }
        return returnFields;
    }

    @Override
    public boolean isBuiltInType() {
        return BuiltInImpl.getInstance().isBuiltInType(javaKeyTxt);
    }

    private List<List<String>> fieldBodyAndDoc(final List<String> noPackageLines) {
        List<List<String>> fieldBodyAndDocs = new ArrayList<>();
        boolean isFindDocBegin = false;
        List<String> fieldLines = null;
        Iterator<String> newNoPackageLines = new ArrayList<>(noPackageLines).iterator();
        while (newNoPackageLines.hasNext()) {
            String line = newNoPackageLines.next();
            Matcher docMatcher = Const.DOC_PATTERN_BEGIN.matcher(line);
            if (!isFindDocBegin && docMatcher.find()) {//匹配到注释
                isFindDocBegin = true;
            }
            if (isFindDocBegin) {
                if (null == fieldLines) {
                    fieldLines = new ArrayList<>();
                    fieldLines.add(line);
                    newNoPackageLines.remove();
                } else if (null != fieldLines && fieldLines.size() > 0) {
                    fieldLines.add(line);
                    newNoPackageLines.remove();
                }
            }
            if (null != fieldLines && fieldLines.size() > 0) {
                for (Pattern methodPattern : Const.FIELD_KEYWORD) {
                    Matcher matcher = methodPattern.matcher(line);
                    if (matcher.find()) {//匹配到属性
                        fieldBodyAndDocs.add(fieldLines);
                        fieldLines = null;
                        isFindDocBegin = false;
                        break;
                    }
                }
            }
        }

        return fieldBodyAndDocs;
    }

    private List<IReturnField> extractDocAndFieldInfo() {
        JavaFileImpl javaFile = new JavaFileImpl();
        javaFile.setJavaFilePath(javaFilePath);
        javaFile.setProjectPath(projectPath);
        javaFile.getIncludePaths().addAll(includePaths);

        if(javaKeyTxt.equals("void")){
            return null;
        }

        File returnTypeFile = javaFile.searchTxtJavaFileForProjectsPath(javaKeyTxt);

        if(null==returnTypeFile){
            throw new JapiException("找不到相关文件："+javaKeyTxt+".java");
        }
        List<String> javaFileLines = null;
        try {
            javaFileLines = FileUtils.readLines(returnTypeFile, Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        final List<List<String>> fieldBodyAndDocs = fieldBodyAndDoc(noPackageLines);

        List<IReturnField> fieldImpls = new ArrayList<>(fieldBodyAndDocs.size());
        for (List<String> fieldLines : fieldBodyAndDocs) {
            ReturnFieldImpl fieldImpl = new ReturnFieldImpl();

            List<IReturnFieldDoc> fieldDocs = extractDoc(fieldLines);//提取属性注释信息
            ReturnFieldImpl extractField = extractField(fieldLines);//提取属性信息

            fieldImpl.setDocs(fieldDocs);
            fieldImpl.setAnnotations(extractField.getAnnotations());
            fieldImpl.setType(extractField.getType());
            if(!BuiltInImpl.getInstance().isBuiltInType(extractField.getType())){//不是java内置类型,属于算定义类型,递归查找
                File childTypeFile = javaFile.searchTxtJavaFileForProjectsPath(extractField.getType());
                if(childTypeFile.getAbsoluteFile().equals(returnTypeFile.getAbsoluteFile())){//自身象
                    fieldImpl.setName("$this");
                }else{
                    ReturnTypeImpl returnTypeImpl =  new ReturnTypeImpl();
                    returnTypeImpl.setJavaFilePath(returnTypeFile.getAbsolutePath());
                    returnTypeImpl.setProjectPath(projectPath);
                    returnTypeImpl.setIncludePaths(includePaths);
                    returnTypeImpl.setJavaKeyTxt(extractField.getType());
                    fieldImpl.setReturnFields(returnTypeImpl.getFields());
                }
            }else{
                fieldImpl.setName(extractField.getName());
            }


            fieldImpls.add(fieldImpl);
        }
        return fieldImpls;
    }

    private List<IReturnFieldDoc> extractDoc(final List<String> fieldLines) {
        boolean fieldBegin = false;
        List<IReturnFieldDoc> fieldDocs = new ArrayList<>();
        for (String fieldLine : fieldLines) {
            Matcher matcherDocBegin = Const.DOC_PATTERN_BEGIN.matcher(fieldLine);
            if (!fieldBegin && matcherDocBegin.find()) {
                fieldBegin = true;
                continue;
            }
            if (fieldBegin) {
                Matcher matcherDocEnd = Const.DOC_PATTERN_END.matcher(fieldLine);
                if (matcherDocEnd.find()) {
                    break;
                }
            }
            if (fieldBegin) {
                ReturnFieldDocImpl docImpl = new ReturnFieldDocImpl();
                Matcher methodFunDesMatcher = Const.DOC_METHOD_FUN_DES.matcher(fieldLine);//方法功能描述
                if (methodFunDesMatcher.find()) {
                    Matcher methodMoreMatcher = Const.DOC_MORE.matcher(fieldLine);
                    if (methodMoreMatcher.find()) {
                        docImpl.setName(methodFunDesMatcher.group().substring(methodMoreMatcher.group().length()));
                    }
                } else {
                    Matcher methodMoreMatcher = Const.DOC_MORE.matcher(fieldLine);//注释左   *
                    if (methodMoreMatcher.find()) {//   *
                        docImpl.setName(fieldLine.substring(methodMoreMatcher.group().length()));
                        Matcher methodNameMatcher = Const.DOC_NAME.matcher(fieldLine);//注释名称 * \@param
                        if (methodNameMatcher.find()) {
                            String methodNameValue = methodNameMatcher.group();
                            String docName = methodNameValue.substring(3);
                            docImpl.setName(docName);
                            Matcher methodNameValueMatcher = Const.DOC_NAME_VALUE.matcher(fieldLine);//注释名称 * \@param user
                            if (methodNameValueMatcher.find()) {
                                String docValue = methodNameValueMatcher.group().substring(methodNameValue.length());
                                if (fieldLine.endsWith(docValue)) {
                                    docImpl.setValue(docValue.trim());
                                } else {
                                    String val = fieldLine.substring(fieldLine.indexOf(docValue)).trim().substring(docValue.length());
                                    docImpl.setValue(val.trim());
                                }
                            }
                        }
                    }
                }
                if (StringUtils.isBlank(docImpl.getName()) && StringUtils.isBlank(docImpl.getValue()) && StringUtils.isBlank(docImpl.getDes())) {
                    continue;//去掉无效的换行注释
                }
                fieldDocs.add(docImpl);
            }
        }
        return fieldDocs;
    }

    private ReturnFieldImpl extractField(List<String> fieldLines) {
        ReturnFieldImpl fieldImpl = new ReturnFieldImpl();
        List<String> annotationStrs = new ArrayList<>();
        String fieldLineStr = null;
        boolean docBegin = false;
        for (String fieldLine : fieldLines) {
            Matcher fieldDocEnd = Const.DOC_PATTERN_END.matcher(fieldLine);
            if (!docBegin && fieldDocEnd.find()) {
                docBegin = true;
                continue;
            }
            if (docBegin) {
                Matcher annotationMatcher = Const.ANNOTATION_PATTERN.matcher(fieldLine);
                if (annotationMatcher.find()) {//注解
                    annotationStrs.add(fieldLine.trim().substring(1));
                } else {//方法
                    fieldLineStr = fieldLine.trim();
                }
            }
        }
        String returnTypeStr = getFieldTypeStr(fieldLineStr);
//        IReturnType returnType = getFieldType(returnTypeStr);
        fieldImpl.setType(returnTypeStr);
        fieldImpl.setAnnotations(annotationStrs);
        //fieldImpl.setReturnFields(returnType.getFields());
        fieldImpl.setName(fieldLineStr.split(StringUtils.SPACE)[1]);
        return fieldImpl;
    }

    private String getFieldTypeStr(final String fieldLineStr) {
        String typeStr = null;
        for (Pattern typePattern : Const.FIELD_KEYWORD) {
            Matcher typeMatch = typePattern.matcher(fieldLineStr);
            if (typeMatch.find()) {
                typeStr = StringUtils.substring(typeMatch.group(), 0, -1).trim();//public String testUser
                break;
            }
        }
        String returnType = null;
        if (StringUtils.isNotBlank(typeStr)) {
            if(typeStr.split(" ").length==2){
                returnType = typeStr.substring(0,typeStr.trim().lastIndexOf(" "));
            }else{
                returnType = typeStr.substring(typeStr.indexOf(StringUtils.SPACE), typeStr.lastIndexOf(StringUtils.SPACE));
            }
            returnType = returnType.trim();
        }
        return returnType;
    }

    private IReturnType getFieldType(final String returnTypeStr) {
        ReturnTypeImpl returnTypeImpl =  new ReturnTypeImpl();
        returnTypeImpl.setJavaFilePath(javaFilePath);
        returnTypeImpl.setProjectPath(projectPath);
        returnTypeImpl.setIncludePaths(includePaths);
        returnTypeImpl.setJavaKeyTxt(returnTypeStr);
        return returnTypeImpl;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }

    public void setJavaKeyTxt(String javaKeyTxt) {
        this.javaKeyTxt = javaKeyTxt;
    }
}
