package com.dounine.japi.core.valid.jsr303.list;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.valid.IMVC;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-17.
 */
public class NotBlankValid implements IMVC {

    private String javaFilePath;
    private String projectPath;
    private List<String> includePaths = new ArrayList<>();

    @Override
    public String getRequestParamName() {
        return "org.hibernate.validator.constraints.NotBlank";
    }

    private List<String> getInterfacePaths(List<String> interfaceGroups) {
        List<String> paths = new ArrayList<>();
        for (String interfaceGroup : interfaceGroups) {
            JavaFileImpl javaFile = new JavaFileImpl();
            javaFile.setJavaFilePath(javaFilePath);
            javaFile.setProjectPath(projectPath);
            javaFile.setIncludePaths(includePaths);
            String key = interfaceGroup.substring(0, interfaceGroup.lastIndexOf("."));
            File file = javaFile.searchTxtJavaFileForProjectsPath(key);
            if (null != file) {
                paths.add(file.getAbsolutePath());
            }
        }
        return paths;
    }

    @Override
    @NotBlank(message = "", groups = {IMVC.class})
    public String getRequestInfoForField(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs, List<String> interfaceGroups) {
        StringBuffer sb = new StringBuffer("{");
        String newNameStr = nameStr;
        String defaultValue = "";
        String description = "";
        String required = "true";

        List<String> myGroupInterfaces = new ArrayList<>();
        if (null != interfaceGroups) {
            required = "false";
            Pattern pattern = JapiPattern.getPattern("groups\\s*[=]\\s*");
            Matcher matcher = pattern.matcher(annoStr);
            if (matcher.find()) {
                Pattern leftPattern = JapiPattern.getPattern("groups\\s*[=]\\s*[{]");
                Matcher leftMatcher = leftPattern.matcher(annoStr);
                if (leftMatcher.find()) {//interfaces
                    String groupAndInterface = annoStr.substring(annoStr.indexOf("{")+1,annoStr.lastIndexOf("}"));
                    myGroupInterfaces.addAll(Arrays.asList(groupAndInterface.split(",")));
                } else {//single interface
                    Pattern groupAndInterfacePattern = JapiPattern.getPattern("groups\\s*[=]\\s*[a-zA-Z0-9_]*[.]class");
                    Matcher groupAndInterfaceMatcher = groupAndInterfacePattern.matcher(annoStr);
                    if (groupAndInterfaceMatcher.find()) {
                        String groupAndInterface = groupAndInterfaceMatcher.group().split("=")[1].trim();
                        myGroupInterfaces.add(groupAndInterface);
                    }
                }
            }
        }

        List<String> myInterfaceGroupPaths = getInterfacePaths(myGroupInterfaces);
        boolean hasGroup = false;
        if (null != myInterfaceGroupPaths && null != interfaceGroups) {
            for (String myPath : myInterfaceGroupPaths) {
                for (String actionPath : interfaceGroups) {
                    if (myPath.equals(actionPath)) {
                        hasGroup = true;
                        break;
                    }
                }
            }
        }
        if (hasGroup) {
            required = "true";
        }

        for (IFieldDoc fieldDoc : docs) {
            if (StringUtils.isBlank(fieldDoc.getValue())) {
                description = fieldDoc.getName();
                break;
            }
        }

        sb.append("\"type\":\"");
        sb.append(TypeConvert.getHtmlType(typeStr));
        sb.append("\",");
        sb.append("\"description\":\"");
        sb.append(description);
        sb.append("\",");
        sb.append("\"required\":");
        sb.append(required);
        sb.append(",");
        sb.append("\"defaultValue\":");
        sb.append("\"");
        sb.append(defaultValue);
        sb.append("\",");
        sb.append("\"name\":\"");
        sb.append(newNameStr);
        sb.append("\"");
        sb.append("}");
        return sb.toString();
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
