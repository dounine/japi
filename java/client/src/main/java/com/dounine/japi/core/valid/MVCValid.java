package com.dounine.japi.core.valid;

import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IParameter;
import com.dounine.japi.core.impl.ParameterImpl;
import com.dounine.japi.core.valid.mvc.RequestParamValid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by lake on 17-2-10.
 */
public class MVCValid implements IValid {

    private static final Logger LOGGER = LoggerFactory.getLogger(MVCValid.class);

    private String projectPath;
    private String javaFilePath;
    private List<String> includePaths = new ArrayList<>();

    @Override
    public List<IMVC> getTypes() {
        List<IMVC> imvcs = new ArrayList<>();
        imvcs.add(new RequestParamValid(projectPath,javaFilePath,includePaths));
        return imvcs;
    }

    @Override
    public IParameter getParameter(String parameterStr,List<String> docsStrs) {
        Matcher typeAndNameMatcher = JapiPattern.TYPE_NAME_PATTERN.matcher(parameterStr);
        typeAndNameMatcher.find();
        String typeAndName = typeAndNameMatcher.group();
        String typeStr = typeAndName.substring(0, typeAndName.indexOf(" "));
        String nameStr = typeAndName.substring(typeStr.length() + 1).trim();

//        TypeImpl returnTypeImpl = new TypeImpl();
//        returnTypeImpl.setJavaFilePath(javaFilePath);
//        returnTypeImpl.setProjectPath(projectPath);
//        returnTypeImpl.setIncludePaths(includePaths);
//        returnTypeImpl.setJavaKeyTxt(typeStr);

        ParameterImpl parameter = new ParameterImpl();
//        if (returnTypeImpl.isBuiltInType()) {
//            returnTypeImpl.setJavaType(typeStr);
//        }
        List<String> requestInfos = getRequestInfos(StringUtils.substring(parameterStr, 0, -typeAndName.length()),typeStr,nameStr);
        parameter.setRequestInfos(requestInfos);
//        parameter.setAnnos(annos);
//        parameter.setType(returnTypeImpl);
//        parameter.setName(nameStr);

        return parameter;
    }

    private List<String> getRequestInfos(String parameterStrExcTypeAndName,String typeStr,String nameStr) {
        Matcher singleAnnoMatcher = JapiPattern.getPattern("@[a-zA-Z0-9_]*").matcher(parameterStrExcTypeAndName);
        List<String> annos = new ArrayList<>();
        int preIndex = -1, nextIndex = -1;
        while (singleAnnoMatcher.find()) {
            nextIndex = singleAnnoMatcher.start();
            if (-1 != preIndex) {
                annos.add(parameterStrExcTypeAndName.substring(preIndex, nextIndex).trim());
                preIndex = nextIndex;
            } else {
                preIndex = 0;
            }
        }
        if (nextIndex != -1) {
            annos.add(parameterStrExcTypeAndName.substring(nextIndex).trim());
        }
        List<String> requestInfos = new ArrayList<>();
        for (String annoStr : annos) {
            if(isValid(annoStr)){//全部使用默认值
                IMVC imvc = getValid(annoStr.substring(1));
                if(null!=imvc){
                    String requestInfo = imvc.getRequestInfo(annoStr,typeStr,nameStr,null);
                    if(StringUtils.isNotBlank(requestInfo)){
                        requestInfos.add(requestInfo);
                    }
                }
            }else{
                LOGGER.warn(annoStr+ " 不在MVCValid识别范围内.");
            }
        }
        return requestInfos;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<String> includePaths) {
        this.includePaths = includePaths;
    }
}

