package com.dounine.japi.core.valid.jsr303.list;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.serial.request.RequestImpl;
import com.dounine.japi.core.valid.IMVC;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
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

    @Override
    public String getRequestParamName() {
        return "org.hibernate.validator.constraints.NotBlank";
    }

    @Override
    public RequestImpl getRequestFieldForAnno(String annoStr, String typeStr, String nameStr, List<IFieldDoc> fieldDocs, List<String> interfaceGroups) {
        RequestImpl requestField = new RequestImpl();
        String newNameStr = nameStr;
        String defaultValue = "";
        String description = getDescription(fieldDocs);
        boolean required = isRequired(annoStr,interfaceGroups,javaFilePath);

        requestField.setType(TypeConvert.getHtmlType(typeStr));
        requestField.setDescription(description);
        requestField.setRequired(required);
        requestField.setConstraint("非空字符串");
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
