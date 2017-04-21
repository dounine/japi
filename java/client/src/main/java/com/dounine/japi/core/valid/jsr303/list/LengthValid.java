package com.dounine.japi.core.valid.jsr303.list;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.SearchInfo;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.serial.request.RequestImpl;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-17.
 */
public class LengthValid implements IMVC {

    private String javaFilePath;

    @Override
    public String getRequestParamName() {
        return "javax.validation.constraints.Length";
    }

    @Override
    public RequestImpl getRequestFieldForAnno(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs, List<SearchInfo> interfaceGroups) {
        RequestImpl requestField = new RequestImpl();
        String newNameStr = nameStr;
        String defaultValue = "";
        StringBuffer constraint = new StringBuffer();
        String description = getDescription(docs);
        boolean required = isRequired(annoStr, interfaceGroups, javaFilePath);

        Pattern minPattern = JapiPattern.getPattern("min\\s*=\\s*\\d+");
        Matcher minMatcher = minPattern.matcher(annoStr);

        if(minMatcher.find()){
            String str = minMatcher.group().split("=")[1].trim();
            constraint.append("最小长度:");
            constraint.append(str);
        }else{
            constraint.append("最小长度:0");
        }

        Pattern maxPattern = JapiPattern.getPattern("max\\s*=\\s*\\d+");
        Matcher maxMatcher = maxPattern.matcher(annoStr);
        if(maxMatcher.find()){
            if(constraint.length()>0){
                constraint.append(",");
            }
            String str = maxMatcher.group().split("=")[1].trim();
            constraint.append("最大长度:");
            constraint.append(str);
        }else{
            constraint.append("最大长度:");
            constraint.append(Integer.MAX_VALUE);
        }

        String arrStr = typeStr.startsWith("array ")?"[]":"";
        typeStr = typeStr.startsWith("array ")?typeStr.substring(6):typeStr;
        requestField.setType(TypeConvert.getHtmlType(typeStr)+arrStr);
        requestField.setDescription(description);
        requestField.setConstraint(constraint.toString());
        requestField.setRequired(required);
        requestField.setDefaultValue(defaultValue);
        requestField.setName(newNameStr);
        return requestField;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

}
