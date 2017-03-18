package com.dounine.japi.core.impl;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IConfig;
import com.dounine.japi.core.IField;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.IType;
import com.dounine.japi.core.impl.types.ClassType;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.serial.request.IRequest;
import com.dounine.japi.serial.request.RequestImpl;
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
public class TypeImpl implements IType {
    private static final Logger CONSOLE = LoggerFactory.getLogger(TypeImpl.class);

    private File javaFile;
    private File searchFile;
    private String javaKeyTxt;
    private List<IField> returnFields;

    /**
     * 自身引用对象
     */
    private static final String MY_SELF_REF = "$this";

    @Override
    public List<IField> getFields() {
        if (null == javaKeyTxt) {
            CONSOLE.error("javaKeyTxt 不能为空");
            throw new JapiException("javaKeyTxt 不能为空");
        }

        if (BuiltInJavaImpl.getInstance().isBuiltInType(javaKeyTxt)) {
            return null;
        }
        if (null == returnFields) {
            returnFields = extractDocAndFieldInfo();//提取属性注释及属性信息
        }
        return returnFields;
    }

    @Override
    public String getName() {

        if(BuiltInJavaImpl.getInstance().isBuiltInType(javaKeyTxt)){
            return javaKeyTxt;
        }
        if("void".equals(javaKeyTxt)){
            return javaKeyTxt;
        }

        searchFile = JavaFileImpl.getInstance().searchTxtJavaFileForProjectsPath(javaKeyTxt, javaFile.getAbsolutePath()).getFile();

        if (null == searchFile) {
            throw new JapiException(javaFile.getAbsolutePath()+" 找不到相关文件：" + javaKeyTxt + ".java");
        }
        List<String> javaFileLines = null;
        try {
            javaFileLines = FileUtils.readLines(searchFile, Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != javaFileLines) {
            Pattern docBeginPattern = JapiPattern.getPattern("[/][*][*]");
            Pattern classBeginPattern = JapiPattern.getPattern("[a-zA-Z0-9_]+\\s*[{]$");
            List<String> docsAndAnnos = new ArrayList<>();
            boolean docBegin = false;
            boolean classBegin = false;
            for (String line : javaFileLines) {
                if (false == docBegin && docBeginPattern.matcher(line).find()) {
                    docBegin = true;
                }
                if (docBegin && !classBegin) {
                    docsAndAnnos.add(line);
                }
                if (classBeginPattern.matcher(line).find()) {
                    classBegin = true;
                }
            }
            if (docsAndAnnos.size() > 0) {
                Pattern docEndPattern = JapiPattern.getPattern("[*][/]$");
                List<String> docs = new ArrayList<>();
                for (String line : docsAndAnnos) {
                    docs.add(line);
                    if (docEndPattern.matcher(line).find()) {
                        break;
                    }
                }
                String name = "";
                for (String line : docs) {
                    if (line.length() > 3) {
                        name = line.substring(3).trim();
                        break;
                    }
                }
                return name;
            }
        }
        return null;
    }

    private List<List<String>> fieldBodyAndDoc(final List<String> noPackageLines) {
        List<List<String>> fieldBodyAndDocs = new ArrayList<>();
        boolean isFindDocBegin = false;
        List<String> fieldLines = null;
        Iterator<String> newNoPackageLines = new ArrayList<>(noPackageLines).iterator();
        while (newNoPackageLines.hasNext()) {
            String line = newNoPackageLines.next();
            Matcher docMatcher = JapiPattern.DOC_PATTERN_BEGIN.matcher(line);
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
                for (Pattern methodPattern : JapiPattern.FIELD_KEYWORD) {
                    if(JapiPattern.getPattern("ArrayList<\\s*[a-zA-Z0-9_$]*\\s*>[(]\\s*\\d*\\s*[)];").matcher(line).find()){
                        fieldBodyAndDocs.add(fieldLines);
                        fieldLines = null;
                        isFindDocBegin = false;
                        break;
                    }else{
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
        }

        return fieldBodyAndDocs;
    }

    private List<IField> extractDocAndFieldInfo() {
        if (javaKeyTxt.equals("void")) {
            return null;
        }
        Matcher arrMatcher = JapiPattern.getPattern("[a-zA-Z]*(?=\\[\\])").matcher(javaKeyTxt);
        Matcher listMatcher = JapiPattern.getPattern("(?<=\\<)(\\S+)(?=\\>)").matcher(javaKeyTxt);
        if(arrMatcher.find()){
            javaKeyTxt = arrMatcher.group();
        }else if(listMatcher.find()){
            javaKeyTxt = listMatcher.group();
        }

        SearchInfo searchInfo = JavaFileImpl.getInstance().searchTxtJavaFileForProjectsPath(javaKeyTxt, javaFile.getAbsolutePath());
        searchFile = searchInfo.getFile();

        if (null == searchFile) {
            throw new JapiException(javaFile.getAbsolutePath()+" 找不到相关文件：" + javaKeyTxt + ".java");
        }
        if(searchInfo.getClassType().equals(ClassType.ENUM)){//枚          return null;
            if(null!=searchInfo.getFile()&& ClassType.ENUM.equals(searchInfo.getClassType())){
                RequestImpl requestField = new RequestImpl();
                requestField.setType("string");
                requestField.setDefaultValue("");
                requestField.setConstraint(EnumParser.getInstance().getTypes(searchInfo.getFile()));
                List<IField> fields = new ArrayList<>();
                FieldImpl field = new FieldImpl();
                field.setRequest(requestField);
                field.setEnumType(true);
                fields.add(field);
                return fields;
            }
        }


        List<String> javaFileLines = null;
        try {
            javaFileLines = FileUtils.readLines(searchFile, Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> noPackageLines = new ArrayList<>();
        boolean match = false;//true 找到类的开始，开始查找方法
        for (String line : javaFileLines) {
            if (!match) {
                for (String chart : JapiPattern.MATCH_CHARTS) {
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
        String javaHeaderStr = noPackageLines.get(0);
        Pattern genericPatther = JapiPattern.getPattern("<(\\s|\\S)*>");
        Matcher genericMatcher = genericPatther.matcher(javaHeaderStr);
        String genericStr = null;
        if (genericMatcher.find()) {
            genericStr = genericMatcher.group();
        }
        noPackageLines = noPackageLines.subList(1, noPackageLines.size() - 1);//去掉类头与尾巴
        final List<List<String>> fieldBodyAndDocs = fieldBodyAndDoc(noPackageLines);

        List<IField> fieldImpls = new ArrayList<>(fieldBodyAndDocs.size());
        for (List<String> fieldLines : fieldBodyAndDocs) {
            FieldImpl fieldImpl = new FieldImpl();

            List<IFieldDoc> fieldDocs = extractDoc(fieldLines);//提取属性注释信息
            FieldImpl extractField = extractField(fieldLines);//提取属性信息

            fieldImpl.setDocs(fieldDocs);
            fieldImpl.setAnnotations(extractField.getAnnotations());
            fieldImpl.setType(extractField.getType());
            String name = extractField.getName();
            name = name.contains("(") ? name.substring(3, name.lastIndexOf("(")).toLowerCase() : name;//method
            fieldImpl.setName(name);
            String type = extractField.getType();
            if ((StringUtils.isNotBlank(genericStr) && genericStr.contains(type))) {//generic type
                fieldImpl.setName(name);
                fieldImpl.setType("object");
            } else if (type.startsWith("array ")){
                type = type.split(" ")[1];
                if (!BuiltInJavaImpl.getInstance().isBuiltInType(type)) {//不是java内置类型,属于算定义类型,递归查找
                    SearchInfo searchInfo1 = JavaFileImpl.getInstance().searchTxtJavaFileForProjectsPath(type, searchFile.getAbsolutePath());
                    File childTypeFile = searchInfo1.getFile();
                    if (null != childTypeFile) {
                        if (childTypeFile.getAbsoluteFile().equals(searchFile.getAbsoluteFile())) {//自身对象
                            fieldImpl.setName(name);
                            fieldImpl.setType(MY_SELF_REF+"[]");
                        } else {
                            TypeImpl returnTypeImpl = new TypeImpl();
                            returnTypeImpl.setJavaFile(searchFile.getAbsoluteFile());
                            returnTypeImpl.setJavaKeyTxt(type);
                            fieldImpl.setFields(returnTypeImpl.getFields());
                        }
                    } else {
                        throw new JapiException(searchFile.getAbsolutePath() + " 找不到相关文件：" + type + ".java");
                    }
                }
            } else if (!BuiltInJavaImpl.getInstance().isBuiltInType(type)) {//不是java内置类型,属于算定义类型,递归查找
                SearchInfo searchInfo1 =JavaFileImpl.getInstance().searchTxtJavaFileForProjectsPath(type, searchFile.getAbsolutePath());
                File childTypeFile = searchInfo1.getFile();
                if(null!=childTypeFile){
                    if (childTypeFile.getAbsoluteFile().equals(searchFile.getAbsoluteFile())) {//自身对象
                        fieldImpl.setName(name);
                        fieldImpl.setType(MY_SELF_REF);
                    } else {
                        TypeImpl returnTypeImpl = new TypeImpl();
                        returnTypeImpl.setJavaFile(searchFile.getAbsoluteFile());
                        returnTypeImpl.setJavaKeyTxt(type);
                        fieldImpl.setFields(returnTypeImpl.getFields());
                    }
                }else {
                    throw new JapiException(searchFile.getAbsolutePath()+" 找不到相关文件：" + type + ".java");
                }

            }


            fieldImpls.add(fieldImpl);
        }
        return fieldImpls;
    }

    private List<IFieldDoc> extractDoc(final List<String> fieldLines) {
        boolean fieldBegin = false;
        List<IFieldDoc> fieldDocs = new ArrayList<>();
        for (String fieldLine : fieldLines) {
            Matcher matcherDocBegin = JapiPattern.DOC_PATTERN_BEGIN.matcher(fieldLine);
            if (!fieldBegin && matcherDocBegin.find()) {
                fieldBegin = true;
                continue;
            }
            if (fieldBegin) {
                Matcher matcherDocEnd = JapiPattern.DOC_PATTERN_END.matcher(fieldLine);
                if (matcherDocEnd.find()) {
                    break;
                }
            }
            if (fieldBegin) {
                FieldDocImpl docImpl = new FieldDocImpl();
                Matcher methodFunDesMatcher = JapiPattern.DOC_METHOD_FUN_DES.matcher(fieldLine);//方法功能描述
                if (methodFunDesMatcher.find()) {
                    Matcher methodMoreMatcher = JapiPattern.DOC_MORE.matcher(fieldLine);
                    if (methodMoreMatcher.find()) {
                        docImpl.setName(methodFunDesMatcher.group().substring(methodMoreMatcher.group().length()));
                    }
                } else {
                    Matcher methodMoreMatcher = JapiPattern.DOC_MORE.matcher(fieldLine);//注释左   *
                    if (methodMoreMatcher.find()) {//   *
                        docImpl.setName(fieldLine.substring(methodMoreMatcher.group().length()));
                        Matcher methodNameMatcher = JapiPattern.DOC_NAME.matcher(fieldLine);//注释名称 * \@param
                        if (methodNameMatcher.find()) {
                            String methodNameValue = methodNameMatcher.group();
                            String docName = methodNameValue.substring(3);
                            docImpl.setName(docName);
                            Matcher methodNameValueMatcher = JapiPattern.DOC_NAME_VALUE.matcher(fieldLine);//注释名称 * \@param user
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

    private FieldImpl extractField(List<String> fieldLines) {
        FieldImpl fieldImpl = new FieldImpl();
        List<String> annotationStrs = new ArrayList<>();
        String fieldLineStr = null;
        boolean docBegin = false;
        for (String fieldLine : fieldLines) {
            Matcher fieldDocEnd = JapiPattern.DOC_PATTERN_END.matcher(fieldLine);
            if (!docBegin && fieldDocEnd.find()) {
                docBegin = true;
                continue;
            }
            if (docBegin) {
                Matcher annotationMatcher = JapiPattern.ANNOTATION_PATTERN.matcher(fieldLine);
                if (annotationMatcher.find()) {//注解
                    annotationStrs.add(fieldLine.trim().substring(1));
                } else {//方法
                    fieldLineStr = fieldLine.trim();
                }
            }
        }
        String returnTypeStr = getFieldTypeStr(fieldLineStr);
        fieldImpl.setType(returnTypeStr);
        fieldImpl.setAnnotations(annotationStrs);
        fieldLineStr = fieldLineStr.endsWith(";") ? StringUtils.substring(fieldLineStr, 0, -1) : fieldLineStr;
        Matcher nameMatcher = JapiPattern.getPattern("(?<=\\>)\\s+\\S+\\s+(?=\\=)").matcher(fieldLineStr);
        if(nameMatcher.find()){
            fieldImpl.setName(nameMatcher.group());
        }else{
            String[] typeOrName = fieldLineStr.split(StringUtils.SPACE);
            fieldImpl.setName(typeOrName[typeOrName.length - 1]);
        }

        return fieldImpl;
    }

    private String getFieldTypeStr(final String fieldLineStr) {
        Matcher arrMatcher = JapiPattern.getPattern("[a-zA-Z]*(?=\\[\\])").matcher(fieldLineStr);
        Matcher listMatcher = JapiPattern.getPattern("(?<=\\<)(\\S+)(?=\\>)").matcher(fieldLineStr);
        if(arrMatcher.find()){
            return "array "+arrMatcher.group();
        }else if(listMatcher.find()){
            return "array "+listMatcher.group();
        }
        String typeStr = null;
        for (Pattern typePattern : JapiPattern.FIELD_KEYWORD) {
            Matcher typeMatch = typePattern.matcher(fieldLineStr);
            if (typeMatch.find()) {
                typeStr = StringUtils.substring(typeMatch.group(), 0, -1).trim();//public String testUser
                break;
            }
        }
        String returnType = null;
        if (StringUtils.isNotBlank(typeStr)) {
            if (typeStr.split(" ").length == 2) {
                returnType = typeStr.substring(0, typeStr.trim().lastIndexOf(" "));
            } else {
                if(typeStr.contains(" ")){
                    returnType = typeStr.substring(typeStr.indexOf(StringUtils.SPACE), typeStr.lastIndexOf(StringUtils.SPACE));
                }else{
                    returnType = typeStr;
                }
            }
            returnType = returnType.trim();
        }
        return returnType;
    }

    public void setJavaKeyTxt(String javaKeyTxt) {
        this.javaKeyTxt = javaKeyTxt;
    }

    public File getJavaFile() {
        return javaFile;
    }

    public void setJavaFile(File javaFile) {
        this.javaFile = javaFile;
    }

    public File getSearchFile() {
        return searchFile;
    }

    public void setSearchFile(File searchFile) {
        this.searchFile = searchFile;
    }
}
