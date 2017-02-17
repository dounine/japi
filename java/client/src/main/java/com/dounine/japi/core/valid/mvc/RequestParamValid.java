package com.dounine.japi.core.valid.mvc;

import com.dounine.japi.common.JapiPattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-13.
 */
public class RequestParamValid implements IMVC {

    public String getRequestInfo(String parameter) {

//        validAnno.getValidAnnoVals().add(new ValidAnnoVal("required", "boolean", "true"));
//        validAnno.getValidAnnoVals().add(new ValidAnnoVal("value", "string", ""));
//        validAnno.getValidAnnoVals().add(new ValidAnnoVal("name", "string", ""));
//        validAnno.getValidAnnoVals().add(new ValidAnnoVal("defaultValue", "string", ""));
//        validAnno.getValidAnnoVals().add(new ValidAnnoVal("value","Class<?>[]","{}"));
        return null;
    }

    @Override
    public String getRequestParamName() {
        return "org.springframework.web.bind.annotation.RequestParam";
    }

    @Override
    public String getRequestInfo(String annoStr, String typeStr, String nameStr) {
        StringBuffer sb = new StringBuffer("{");
        String newNameStr = nameStr;
        String defaultValue = "";
        String required = "true";
        sb.append("\"type\":\"");
        sb.append(typeStr);
        sb.append("\",");
        if (annoStr.trim().endsWith(")")) {//可能带参数
            Pattern namePattern = JapiPattern.getPattern("name(\\s)*=(\\s)*[\"]\\S*[\"]");
            Pattern defaultValuePattern = JapiPattern.getPattern("defaultValue(\\s)*=(\\s)*[\"]\\S*[\"]");
            Pattern requiredPattern = JapiPattern.getPattern("required(\\s)*=(\\s)[truefals]{4,5}");
            Matcher nameMatcher = namePattern.matcher(annoStr);
            Matcher defaultValueMatcher = defaultValuePattern.matcher(annoStr);
            Matcher requiredMatcher = requiredPattern.matcher(annoStr);
            if(nameMatcher.find()){
                String nameMatcherStr = nameMatcher.group();
                newNameStr = nameMatcherStr.substring(nameMatcherStr.indexOf("\""),nameMatcherStr.lastIndexOf("\""));
            }
            if(defaultValueMatcher.find()){
                String defaultValueMatcherStr = defaultValueMatcher.group();
                defaultValue = defaultValueMatcherStr.substring(defaultValueMatcherStr.indexOf("\""),defaultValueMatcherStr.lastIndexOf("\""));
            }
            if(requiredMatcher.find()){
                String requiredMatcherStr = requiredMatcher.group();
                required = requiredMatcherStr.split("=")[1].trim();
            }
        }
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
}
