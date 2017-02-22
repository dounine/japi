package com.dounine.japi.core.valid.jsr303;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IField;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.BuiltInJavaImpl;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.impl.TypeImpl;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.core.valid.jsr303.list.NotBlankValid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-17.
 */
public class ValidValid implements IMVC {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidValid.class);

    private String projectPath;
    private String javaFilePath;
    private List<String> includePaths = new ArrayList<>();

    public ValidValid(String projectPath, String javaFilePath, List<String> includePaths) {
        this.projectPath = projectPath;
        this.javaFilePath = javaFilePath;
        this.includePaths = includePaths;
    }

    private List<IMVC> getJsr303List() {
        List<IMVC> imvcs = new ArrayList<>();
        NotBlankValid notBlankValid = new NotBlankValid();
        notBlankValid.setIncludePaths(includePaths);
        notBlankValid.setProjectPath(projectPath);
        notBlankValid.setJavaFilePath(javaFilePath);
        imvcs.add(notBlankValid);
        return imvcs;
    }

    @Override
    public String getRequestParamName() {
        return "javax.validation.Valid";
    }

    @Override
    public String getRequestInfo(String parameterStrExcTypeAndName,String typeStr, String nameStr, List<String> docs) {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\":\"");
        sb.append(nameStr);
        sb.append(",");
        String description = "";
        if (!BuiltInJavaImpl.getInstance().isBuiltInType(typeStr)) {
            TypeImpl typeImpl = new TypeImpl();
            typeImpl.setJavaFilePath(javaFilePath);
            typeImpl.setProjectPath(projectPath);
            typeImpl.setIncludePaths(includePaths);
            typeImpl.setJavaKeyTxt(typeStr);

            List<IField> fields = typeImpl.getFields();
            List<IMVC> imvcs = getJsr303List();
            sb.append("\"type\":\"object\"");
            sb.append(",");
            List<String> fieldBuffer = new ArrayList<>();
            if (fields.size() > 0) {
                sb.append("\"fields\":[");
                for (IField iField : fields) {
                    IMVC mvc = null;
                    String anno = null;
                    for (IMVC imvc : imvcs) {
                        List<String> annotations = iField.getAnnotations();
                        for (String annotation : annotations) {
                            String _anno = null;
                            if (-1 != annotation.indexOf("(")) {
                                _anno = annotation.substring(0, annotation.indexOf("("));
                            } else {
                                _anno = annotation;
                            }
                            if (_anno.equals(imvc.getRequestParamName()) || imvc.getRequestParamName().endsWith(_anno)) {
                                anno = annotation;
                                mvc = imvc;
                                break;
                            }
                        }
                    }
                    if (null != mvc) {//找到对应jsr303注解
                        fieldBuffer.add(mvc.getRequestInfoForField(anno, iField.getType(), iField.getName(), iField.getDocs(),null));
                    } else {//其它注没有注解
                        if (null != iField.getAnnotations() && iField.getAnnotations().size() > 0) {
                            System.out.println(JSON.toJSONString(iField.getAnnotations()) + "这些注解我都不认识噢.");
                        } else {
                            if (!"$this".equals(iField.getType())) {
                                List<String> _docs = new ArrayList<>();
                                _docs.add("* @param "+iField.getName()+" "+iField.getDocs().get(0).getName());
                                String requestInfo = getRequestInfo(null,iField.getType(), iField.getName(), _docs);
                                if (StringUtils.isNotBlank(requestInfo)) {
                                    fieldBuffer.add(requestInfo);
                                }
                            } else {
                                StringBuffer mySelf = new StringBuffer("{");
                                mySelf.append("\"name\":\"");
                                mySelf.append(iField.getName());
                                mySelf.append("\",");
                                mySelf.append("\"type\":");
                                mySelf.append("\"$this\",");
                                mySelf.append("\"description\":\"");
                                mySelf.append("\"自身对象\",");
                                mySelf.append("\"required\":");
                                mySelf.append("false,");
                                mySelf.append("\"defaultValue\":");
                                mySelf.append("\"\"");
                                mySelf.append("}");
                                fieldBuffer.add(mySelf.toString());
                            }
                        }
                    }
                }
                String childStr = StringUtils.join(fieldBuffer.toArray(), ",");
                sb.append(childStr);
                sb.append("]");
            }
            if (StringUtils.join(fieldBuffer.toArray(), ",").contains("\"required\":true")) {
                sb.append(",\"required\":true");
            }

            sb.append(",\"defaultValue\":\"\"");
            sb.append(",\"description\":\"");
            if (null != docs && docs.size() > 0) {
                for (String doc : docs) {
                    Pattern pattern = JapiPattern.getPattern("[*]\\s*[@]\\S*\\s*" + nameStr);//找到action传进来的注解信息
                    Matcher matcher = pattern.matcher(doc);
                    if (matcher.find()) {
                        description = doc.substring(matcher.end()).trim();
                        break;
                    }
                }
            }
            sb.append(description);
            sb.append("\"");
        }else{
            sb.append("\"type\":\"");
            sb.append(TypeConvert.getHtmlType(typeStr));
            sb.append("\"");
            sb.append(",\"defaultValue\":\"\"");
            sb.append(",\"required\":false");
            sb.append(",\"description\":\"");
            if (null != docs && docs.size() > 0) {
                for (String doc : docs) {
                    Pattern pattern = JapiPattern.getPattern("[*]\\s*[@]\\S*\\s*" + nameStr);//找到action传进来的注解信息
                    Matcher matcher = pattern.matcher(doc);
                    if (matcher.find()) {
                        description = doc.substring(matcher.end()).trim();
                        break;
                    }
                }
            }
            sb.append(description);
            sb.append("\"");
        }
        sb.append("}");
        return sb.toString();
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
