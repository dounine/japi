package com.dounine.japi.core.valid.jsr303.list;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.JavaFileImpl;
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

    private List<String> getInterfacePaths(List<String> interfaceGroups) {
        List<String> paths = new ArrayList<>();
        for (String interfaceGroup : interfaceGroups) {
            String key = interfaceGroup.substring(0, interfaceGroup.lastIndexOf("."));
            File file = JavaFileImpl.getInstance().searchTxtJavaFileForProjectsPath(key, javaFilePath);
            if (null != file) {
                paths.add(file.getAbsolutePath());
            }
        }
        return paths;
    }

    @Override
    public RequestImpl getRequestFieldForField(String annoStr, String typeStr, String nameStr, List<IFieldDoc> docs, List<String> interfaceGroups) {
        RequestImpl requestField = new RequestImpl();
        String newNameStr = nameStr;
        String defaultValue = "";
        String description = "";
        boolean required = true;

        List<String> myGroupInterfaces = new ArrayList<>();
        if (null != interfaceGroups && interfaceGroups.size() > 0) {
            required = false;
            Pattern pattern = JapiPattern.getPattern("groups\\s*[=]\\s*");
            Matcher matcher = pattern.matcher(annoStr);
            if (matcher.find()) {
                Pattern leftPattern = JapiPattern.getPattern("groups\\s*[=]\\s*[{]");
                Matcher leftMatcher = leftPattern.matcher(annoStr);
                if (leftMatcher.find()) {//interfaces
                    String groupAndInterface = annoStr.substring(annoStr.indexOf("{") + 1, annoStr.lastIndexOf("}"));
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
            required = true;
        }

        for (IFieldDoc fieldDoc : docs) {
            if (StringUtils.isBlank(fieldDoc.getValue())) {
                description = fieldDoc.getName();
                break;
            }
        }

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
