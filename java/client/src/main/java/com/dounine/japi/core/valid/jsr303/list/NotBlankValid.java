package com.dounine.japi.core.valid.jsr303.list;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.valid.IMVC;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-17.
 */
public class NotBlankValid implements IMVC {
    @Override
    public String getRequestParamName() {
        return "org.hibernate.validator.constraints.NotBlank";
    }

    @Override
    @NotBlank(message = "",groups = {IMVC.class})
    public String getRequestInfo(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs) {
        StringBuffer sb = new StringBuffer("{");
        String newNameStr = nameStr;
        String defaultValue = "";
        String description = "";
        String required = "true";

        for(IFieldDoc fieldDoc : docs){
            if(StringUtils.isBlank(fieldDoc.getValue())){
                description = fieldDoc.getName();
                break;
            }
        }

        sb.append("\"type\":\"");
        sb.append(typeStr);
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
}
