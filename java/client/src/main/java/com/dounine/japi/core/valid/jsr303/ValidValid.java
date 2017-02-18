package com.dounine.japi.core.valid.jsr303;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IField;
import com.dounine.japi.core.IFieldDoc;
import com.dounine.japi.core.impl.BuiltInJavaImpl;
import com.dounine.japi.core.impl.JavaFileImpl;
import com.dounine.japi.core.impl.TypeImpl;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.core.valid.jsr303.list.NotBlankValid;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-17.
 */
public class ValidValid implements IMVC {

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
        imvcs.add(new NotBlankValid());
        return imvcs;
    }

    @Override
    public String getRequestParamName() {
        return "javax.validation.Valid";
    }

    @Override
    public String getRequestInfo(String annoStr, String typeStr, String nameStr,List<IFieldDoc> docs) {
        if (!BuiltInJavaImpl.getInstance().isBuiltInType(typeStr)) {
            TypeImpl typeImpl = new TypeImpl();
            typeImpl.setJavaFilePath(javaFilePath);
            typeImpl.setProjectPath(projectPath);
            typeImpl.setIncludePaths(includePaths);
            typeImpl.setJavaKeyTxt(typeStr);

            List<IField> fields = typeImpl.getFields();
            List<IMVC> imvcs = getJsr303List();

            StringBuffer sb = new StringBuffer("{");
            sb.append("\"name\":\"");
            sb.append(nameStr);
            sb.append(",");
            sb.append("\"type\":\"Object\"");
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
                    if (null != mvc) {
                        fieldBuffer.add(mvc.getRequestInfo(anno, iField.getType(), iField.getName(), iField.getDocs()));
                    } else {//其它注没有注解
                        if (null != iField.getAnnotations() && iField.getAnnotations().size() > 0) {
                            System.out.println(JSON.toJSONString(iField.getAnnotations()) + "这些注解我都不认识噢.");
                        }else{
                            if(!"$this".equals(iField.getType())){
                                String requestInfo = getRequestInfo(annoStr,iField.getType(),iField.getName());
                                if(StringUtils.isNotBlank(requestInfo)){
                                    fieldBuffer.add(requestInfo);
                                }
                            }else{
                                System.out.println(iField.getType()+"自身对象");
                            }
                        }
                    }
                }
                String childStr = StringUtils.join(fieldBuffer.toArray(), ",");
                sb.append(childStr);
                sb.append("]");
            }
            if(StringUtils.join(fieldBuffer.toArray(), ",").contains("\"required\":true")){
                sb.append(",\"required\":true");
            }
            sb.append(",\"defaultValue\":\"\"");
            sb.append(",\"description\":\"");
            sb.append("");
            sb.append("\"");
            sb.append("}");
            System.out.println(sb.toString());
            return sb.toString();
        }
        return null;
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
