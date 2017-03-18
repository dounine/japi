package com.dounine.japi.core.valid.jsr303.list;

import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.SearchInfo;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.serial.request.RequestImpl;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by lake on 17-2-17.
 */
public class PatternValid implements IMVC {


    private String javaFilePath;
    private static final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-z]{6,7}\\s*[=]\\s*");

    @Override
    public String getRequestParamName() {
        return "javax.validation.constraints.Pattern";
    }

    @Override
    public RequestImpl getRequestFieldForAnno(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs, List<SearchInfo> interfaceGroups) {
        RequestImpl requestField = new RequestImpl();
        String newNameStr = nameStr;
        String defaultValue = "";
        StringBuffer constraint = new StringBuffer();
        String description = getDescription(docs);
        boolean required = isRequired(annoStr, interfaceGroups, javaFilePath);

        constraint.append(getRegex(annoStr)+" <==> "+getDes(annoStr));

        requestField.setType(TypeConvert.getHtmlType(typeStr));
        requestField.setDescription(description);
        requestField.setConstraint(constraint.toString());
        requestField.setRequired(required);
        requestField.setDefaultValue(defaultValue);
        requestField.setName(newNameStr);
        return requestField;
    }

    private String getDes(String str){
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-z]{6,7}\\s*[=]\\s*");
        Matcher matcher = pattern.matcher(str);
        boolean isFindRegexp = false;
        int beginIndex = -1,endIndex = -1,count = 0;
        while (matcher.find()) {
            count++;
            if (!isFindRegexp && matcher.group().matches("message\\s*[=]\\s*")) {
                isFindRegexp = true;
                beginIndex = matcher.end();
                continue;
            }
            if(isFindRegexp){
                endIndex = matcher.start();
                break;
            }
        }
        if(count==1){
            return str.substring(beginIndex+1,str.lastIndexOf("\""));
        }else{
            if(endIndex==-1){
                return str.substring(beginIndex+1,str.lastIndexOf("\""));
            }else{
                str = str.substring(beginIndex,endIndex);
                return str.substring(1,str.lastIndexOf("\""));
            }
        }
    }

    private String getRegex(String str){
        Matcher matcher = pattern.matcher(str);
        boolean isFindRegexp = false;
        int beginIndex = -1,endIndex = -1,count = 0;
        while (matcher.find()) {
            count++;
            if (!isFindRegexp && matcher.group().matches("regexp\\s*[=]\\s*")) {
                isFindRegexp = true;
                beginIndex = matcher.end();
                continue;
            }
            if(isFindRegexp){
                endIndex = matcher.start();
                break;
            }
        }
        if(count==1){
            return str.substring(beginIndex+1,str.lastIndexOf("\""));
        }else{
            str = str.substring(beginIndex,endIndex);
            return str.substring(1,str.lastIndexOf("\""));
        }
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

}
