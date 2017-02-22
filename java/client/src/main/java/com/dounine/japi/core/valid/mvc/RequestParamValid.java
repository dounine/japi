package com.dounine.japi.core.valid.mvc;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.valid.IMVC;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-13.
 */
public class RequestParamValid implements IMVC {

    private String projectPath;
    private String javaFilePath;
    private List<String> includePaths = new ArrayList<>();

    public RequestParamValid(String projectPath, String javaFilePath, List<String> includePaths) {
        this.projectPath = projectPath;
        this.javaFilePath = javaFilePath;
        this.includePaths = includePaths;
    }

    @Override
    public String getRequestParamName() {
        return "org.springframework.web.bind.annotation.RequestParam";
    }

    @Override
    public String getRequestInfo(String annoStr, String typeStr, String nameStr, List<String> docsStrs) {
        StringBuffer sb = new StringBuffer("{");
        String newNameStr = nameStr;
        String defaultValue = "";
        String description = "";
        String required = "true";
        sb.append("\"type\":\"");
        sb.append(TypeConvert.getHtmlType(typeStr));
        sb.append("\",");
        if (annoStr.trim().endsWith(")")) {//可能带参数
            Pattern namePattern = JapiPattern.getPattern("name(\\s)*=(\\s)*[\"]\\S*[\"]");
            Pattern defaultValuePattern = JapiPattern.getPattern("defaultValue(\\s)*=(\\s)*[\"]\\S*[\"]");
            Pattern requiredPattern = JapiPattern.getPattern("required(\\s)*=(\\s)[truefals]{4,5}");
            Matcher nameMatcher = namePattern.matcher(annoStr);
            Matcher defaultValueMatcher = defaultValuePattern.matcher(annoStr);
            Matcher requiredMatcher = requiredPattern.matcher(annoStr);
            if (nameMatcher.find()) {
                String nameMatcherStr = nameMatcher.group();
                newNameStr = nameMatcherStr.substring(nameMatcherStr.indexOf("\""), nameMatcherStr.lastIndexOf("\""));
            }
            if (defaultValueMatcher.find()) {
                String defaultValueMatcherStr = defaultValueMatcher.group();
                defaultValue = defaultValueMatcherStr.substring(defaultValueMatcherStr.indexOf("\""), defaultValueMatcherStr.lastIndexOf("\""));
            }
            if (requiredMatcher.find()) {
                String requiredMatcherStr = requiredMatcher.group();
                required = requiredMatcherStr.split("=")[1].trim();
            }
        }
        sb.append("\"description\":\"");
        if (null != docsStrs && docsStrs.size() > 0) {
            for (String doc : docsStrs) {
                Pattern pattern = JapiPattern.getPattern("[*]\\s*[@]\\S*\\s*" + nameStr + "\\s+");//找到action传进来的注解信息
                Matcher matcher = pattern.matcher(doc);
                if (matcher.find()) {
                    description = doc.substring(matcher.end()).trim();
                    break;
                }
            }
        }
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

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }
}
