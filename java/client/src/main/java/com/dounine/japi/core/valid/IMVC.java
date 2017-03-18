package com.dounine.japi.core.valid;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.core.impl.SearchInfo;
import com.dounine.japi.core.valid.jsr303.list.*;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.serial.request.IRequest;
import com.dounine.japi.serial.request.RequestImpl;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-13.
 */
public interface IMVC {

    String getRequestParamName();

    default List<IMVC> getJSR303(String javaFilePath){
        List<IMVC> imvcs = new ArrayList<>();
        NotBlankValid notBlankValid = new NotBlankValid();
        notBlankValid.setJavaFilePath(javaFilePath);
        imvcs.add(notBlankValid);

        NotNullValid notNullValid = new NotNullValid();
        notNullValid.setJavaFilePath(javaFilePath);
        imvcs.add(notNullValid);

        PatternValid patternValid = new PatternValid();
        patternValid.setJavaFilePath(javaFilePath);
        imvcs.add(patternValid);

        SizeValid sizeValid = new SizeValid();
        sizeValid.setJavaFilePath(javaFilePath);
        imvcs.add(sizeValid);

        MaxValid maxValid = new MaxValid();
        maxValid.setJavaFilePath(javaFilePath);
        imvcs.add(maxValid);

        MinValid minValid = new MinValid();
        minValid.setJavaFilePath(javaFilePath);
        imvcs.add(minValid);

        EmailValid emailValid = new EmailValid();
        emailValid.setJavaFilePath(javaFilePath);
        imvcs.add(emailValid);

        AssertFalseValid assertFalseValid = new AssertFalseValid();
        assertFalseValid.setJavaFilePath(javaFilePath);
        imvcs.add(assertFalseValid);

        AssertTrueValid assertTrueValid = new AssertTrueValid();
        assertTrueValid.setJavaFilePath(javaFilePath);
        imvcs.add(assertTrueValid);

        LengthValid lengthValid = new LengthValid();
        lengthValid.setJavaFilePath(javaFilePath);
        imvcs.add(lengthValid);

        return imvcs;
    }

    default IRequest getRequestField(String annoStr, String typeStr, String nameStr, List<String> docs, File javaFile){
        return null;
    }

    default IRequest getRequestFieldForAnno(String annoStr, String typeStr, String nameStr, List<IFieldDoc> fieldDocs, List<SearchInfo> interfaceGroups){
        return null;
    }

    default List<SearchInfo> getInterfacePaths(List<String> interfaceGroups,String javaFilePath) {
        List<SearchInfo> searchInfos = new ArrayList<>();
        for (String interfaceGroup : interfaceGroups) {
            String key = interfaceGroup.substring(0, interfaceGroup.lastIndexOf("."));
            SearchInfo searchInfo = JavaFileImpl.getInstance().searchTxtJavaFileForProjectsPath(key, javaFilePath);
            if (null != searchInfo.getFile()) {
                searchInfos.add(searchInfo);
            }else{
                throw new JapiException(javaFilePath+" 找不到相关文件：" + key + ".java");
            }
        }
        return searchInfos;
    }

    default boolean isRequired(String annoStr,List<SearchInfo> interfaceGroups,String javaFilePath){
        boolean isRequire = true;
        List<String> myGroupInterfaces = new ArrayList<>();
        if (null != interfaceGroups && interfaceGroups.size() > 0) {
            isRequire = false;
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

        List<SearchInfo> myInterfaceGroupPaths = getInterfacePaths(myGroupInterfaces,javaFilePath);
        if (null != myInterfaceGroupPaths && null != interfaceGroups) {
            for (SearchInfo myPath : myInterfaceGroupPaths) {
                for (SearchInfo searchInfo : interfaceGroups) {
                    if (myPath.getFile().equals(searchInfo.getFile())&&myPath.getKey().equals(searchInfo.getKey())) {
                        isRequire = true;
                        break;
                    }
                }
            }
        }
        return isRequire;
    }

    default String getDescription(List<IFieldDoc> fieldDocs){
        String description = "";
        for (IFieldDoc fieldDoc : fieldDocs) {
            if (StringUtils.isBlank(fieldDoc.getValue())) {
                description = fieldDoc.getName();
                break;
            }
        }
        return description;
    }
}
