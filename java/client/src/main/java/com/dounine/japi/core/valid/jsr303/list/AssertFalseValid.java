package com.dounine.japi.core.valid.jsr303.list;

import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.serial.request.RequestImpl;

import java.util.List;

/**
 * Created by lake on 17-2-17.
 */
public class AssertFalseValid implements IMVC {

    private String javaFilePath;

    @Override
    public String getRequestParamName() {
        return "javax.validation.constraints.AssertFalse";
    }

    @Override
    public RequestImpl getRequestFieldForAnno(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs, List<String> interfaceGroups) {
        RequestImpl requestField = new RequestImpl();
        String newNameStr = nameStr;
        String defaultValue = "";
        StringBuffer constraint = new StringBuffer();
        String description = getDescription(docs);
        boolean required = isRequired(annoStr, interfaceGroups, javaFilePath);

        constraint.append("值只能为:false");

        requestField.setType(TypeConvert.getHtmlType(typeStr));
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
