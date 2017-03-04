package com.dounine.japi.core.valid.mvc;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.serial.request.RequestImpl;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-3-4.
 */
public class PathVariableValid implements IMVC {
    @Override
    public String getRequestParamName() {
        return "org.springframework.web.bind.annotation.PathVariable";
    }

    @Override
    public RequestImpl getRequestField(String annoStr, String typeStr, String nameStr, List<String> docs, File javaFile) {
        RequestImpl requestField = new RequestImpl();
        String newNameStr = nameStr;
        String defaultValue = "";
        String description = "";
        boolean required = true;
        requestField.setType(TypeConvert.getHtmlType(typeStr));
        if (annoStr.trim().endsWith(")")) {//可能带参数
            Pattern namePattern = JapiPattern.getPattern("name(\\s)*=(\\s)*[\"]\\S*[\"]");
            Pattern requiredPattern = JapiPattern.getPattern("required(\\s)*=(\\s)[truefals]{4,5}");
            Matcher nameMatcher = namePattern.matcher(annoStr);
            Matcher requiredMatcher = requiredPattern.matcher(annoStr);
            if (nameMatcher.find()) {
                String nameMatcherStr = nameMatcher.group();
                newNameStr = nameMatcherStr.substring(nameMatcherStr.indexOf("\""), nameMatcherStr.lastIndexOf("\""));
            }
            if (requiredMatcher.find()) {
                String requiredMatcherStr = requiredMatcher.group();
                required = Boolean.parseBoolean(requiredMatcherStr.split("=")[1].trim());
            }
        }
        if (null != docs && docs.size() > 0) {
            for (String doc : docs) {
                Pattern pattern = JapiPattern.getPattern("[*]\\s*[@]\\S*\\s*" + nameStr + "\\s+");//找到action传进来的注解信息
                Matcher matcher = pattern.matcher(doc);
                if (matcher.find()) {
                    description = doc.substring(matcher.end()).trim();
                    break;
                }
            }
        }
        requestField.setDescription(description);
        requestField.setRequired(required);
        requestField.setDefaultValue(defaultValue);
        requestField.setName(newNameStr);
        return requestField;
    }
}
