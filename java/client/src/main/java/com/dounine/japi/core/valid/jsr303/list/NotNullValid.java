package com.dounine.japi.core.valid.jsr303.list;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.EnumParser;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.core.impl.SearchInfo;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.serial.request.RequestImpl;
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
public class NotNullValid implements IMVC {

    private String javaFilePath;

    @Override
    public String getRequestParamName() {
        return "javax.validation.constraints.NotNull";
    }


    @Override
    public RequestImpl getRequestFieldForAnno(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs, List<SearchInfo> interfaceGroups) {
        RequestImpl requestField = new RequestImpl();
        String newNameStr = nameStr;
        String defaultValue = "";
        String description = getDescription(docs);
        boolean required = isRequired(annoStr,interfaceGroups,javaFilePath);

        String arrStr = typeStr.startsWith("array ")?"[]":"";
        typeStr = typeStr.startsWith("array ")?typeStr.substring(6):typeStr;
        String s = TypeConvert.getHtmlType(typeStr);
        String enumContent = null;
        if(s.equals("object")){
            SearchInfo searchInfo = JavaFileImpl.getInstance().searchTxtJavaFileForProjectsPath(typeStr,javaFilePath);
            if(null!=searchInfo.getFile()){
                enumContent= EnumParser.getInstance().getTypes(searchInfo.getFile());
                s = "string";
            }
        }
        requestField.setType(s+arrStr);
        requestField.setDescription(description);
        requestField.setRequired(required);
        String alias = null;
        if(s.equals("string")){
            alias = "字符串";
        }else if(s.equals("number")){
            alias = "数字";
        }else if(s.equals("boolean")){
            alias = "布而";
        }else if(s.equals("object")){
            alias = "对象";
        }
        if(arrStr.equals("[]")){
            requestField.setConstraint("非空"+alias+"数组");
        }else{
            if(null!=enumContent){
               requestField.setConstraint(enumContent);
            }else{
                requestField.setConstraint("非空"+alias);
            }
        }
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
