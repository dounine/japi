package com.dounine.japi.core.valid.jsr303;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.IField;
import com.dounine.japi.core.impl.BuiltInJavaImpl;
import com.dounine.japi.core.impl.TypeConvert;
import com.dounine.japi.core.impl.TypeImpl;
import com.dounine.japi.serial.request.RequestImpl;
import com.dounine.japi.core.valid.IMVC;
import com.dounine.japi.core.valid.jsr303.list.NotBlankValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lake on 17-2-17.
 */
public class ValidValid implements IMVC {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidValid.class);

    private String javaFilePath;

    public ValidValid(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    private List<IMVC> getJsr303List() {
        List<IMVC> imvcs = new ArrayList<>();
        NotBlankValid notBlankValid = new NotBlankValid();
        notBlankValid.setJavaFilePath(javaFilePath);
        imvcs.add(notBlankValid);
        return imvcs;
    }

    @Override
    public String getRequestParamName() {
        return "javax.validation.Valid";
    }

    @Override
    public RequestImpl getRequestField(String parameterStrExcTypeAndName, String typeStr, String nameStr, List<String> docs, File javaFile) {
        RequestImpl requestField = new RequestImpl();
        requestField.setName(nameStr);
        String description = "";
        if (!BuiltInJavaImpl.getInstance().isBuiltInType(typeStr)) {
            TypeImpl typeImpl = new TypeImpl();
            typeImpl.setJavaFile(javaFile);
            typeImpl.setJavaKeyTxt(typeStr);

            List<IField> fields = typeImpl.getFields();
            List<IMVC> imvcs = getJsr303List();
            requestField.setType("object");
            List<RequestImpl> requestFields = new ArrayList<>();
            if (fields.size() > 0) {
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
                        requestFields.add(mvc.getRequestFieldForField(anno, iField.getType(), iField.getName(), iField.getDocs(),null));
                    } else {//其它注没有注解
                        if (null != iField.getAnnotations() && iField.getAnnotations().size() > 0) {
                            System.out.println(JSON.toJSONString(iField.getAnnotations()) + "这些注解我都不认识噢.");
                        } else {
                            if (!"$this".equals(iField.getType())) {
                                List<String> _docs = new ArrayList<>();
                                _docs.add("* @param "+iField.getName()+" "+iField.getDocs().get(0).getName());
                                RequestImpl _requestField = getRequestField(null,iField.getType(), iField.getName(), _docs,typeImpl.getSearchFile());
                                if(null!=_requestField){
                                    requestFields.add(_requestField);
                                }
                            } else {
                                RequestImpl _requestField = new RequestImpl();
                                _requestField.setName(iField.getName());
                                _requestField.setType("$this");
                                _requestField.setDescription("自身对象");
                                _requestField.setRequired(false);
                                _requestField.setDefaultValue("");
                                requestFields.add(_requestField);
                            }
                        }
                    }
                }
                requestField.setFields(requestFields);
            }
//            if (StringUtils.join(fieldBuffer.toArray(), ",").contains("required:true")) {
//                sb.append(",required:true");
//            }
            requestField.setDefaultValue("");
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
            requestField.setDescription(description);
        }else{
            requestField.setType(TypeConvert.getHtmlType(typeStr));
            requestField.setDefaultValue("");
            requestField.setRequired(false);
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
            requestField.setDescription(description);
        }
        return requestField;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }
}
