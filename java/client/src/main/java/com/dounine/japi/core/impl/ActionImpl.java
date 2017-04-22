package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.JapiClient;
import com.dounine.japi.common.JapiPattern;
import com.dounine.japi.core.*;
import com.dounine.japi.core.annotation.IActionRequest;
import com.dounine.japi.core.annotation.impl.ActionRequest;
import com.dounine.japi.serial.ActionInfo;
import com.dounine.japi.serial.ActionInfoDoc;
import com.dounine.japi.serial.ActionInfoRequest;
import com.dounine.japi.serial.request.IRequest;
import com.dounine.japi.serial.response.IResponse;
import com.dounine.japi.serial.response.ResponseImpl;
import com.dounine.japi.serial.type.DocType;
import com.dounine.japi.serial.type.RequestMethod;
import com.dounine.japi.core.valid.IValid;
import com.dounine.japi.core.valid.JSR303Valid;
import com.dounine.japi.core.valid.MVCValid;
import com.dounine.japi.core.valid.comm.DefaultValid;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class ActionImpl implements IAction {

    private File actionFile;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionImpl.class);

    @Override
    public List<IActionMethod> getMethods() {
        List<IActionMethod> methods = null;
        try {
            List<String> javaFileLines = FileUtils.readLines(actionFile, Charset.forName("utf-8"));
            List<String> noPackageLines = new ArrayList<>();
            List<String> javaAnnoLines = new ArrayList<>();
            boolean match = false;//true 找到类的开始，开始查找方法
            for (String line : javaFileLines) {
                if (!match) {
                    for (String chart : JapiPattern.MATCH_CHARTS) {
                        if (line.startsWith(chart)) {
                            match = true;
                            break;
                        }
                    }
                }
                if (match) {
                    noPackageLines.add(line);
                } else {
                    if (JapiPattern.getPattern("@[a-zA-Z0-9_]*").matcher(line).find()) {
                        javaAnnoLines.add(line);
                    }
                }
            }
            noPackageLines = noPackageLines.subList(1, noPackageLines.size() - 1);//去掉类头与尾巴
            List<List<String>> methodBodyAndDocs = methodBodyAndDoc(noPackageLines);
            methods = extractDocAndMethodInfo(methodBodyAndDocs, javaAnnoLines);//提取方法注释及方法信息

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new JapiException(e.getMessage());
        }
        return methods;
    }

    /**
     * 提供类的方法信息
     *
     * @param noPackageLines 不包含package头部与类尾部行信息
     * @return 方法信息列表
     */
    private List<List<String>> methodBodyAndDoc(final List<String> noPackageLines) {
        List<List<String>> methodBodyAndDocs = new ArrayList<>();
        boolean isFindDocBegin = false;
        List<String> methodLines = null;
        Iterator<String> newNoPackageLines = new ArrayList<>(noPackageLines).iterator();
        while (newNoPackageLines.hasNext()) {
            String line = newNoPackageLines.next();
            Matcher docMatcher = JapiPattern.DOC_PATTERN_BEGIN.matcher(line);
            if (!isFindDocBegin && docMatcher.find()) {//匹配到注释
                isFindDocBegin = true;
            }
            if (isFindDocBegin) {
                if (null == methodLines) {
                    methodLines = new ArrayList<>();
                    if(StringUtils.isNotBlank(line)){
                        methodLines.add(line);
                    }
                    newNoPackageLines.remove();
                } else if (null != methodLines && methodLines.size() > 0) {
                    if(StringUtils.isNotBlank(line)){
                        methodLines.add(line);
                    }
                    newNoPackageLines.remove();
                }
            }
            if (null != methodLines && methodLines.size() > 0) {
                for (Pattern methodPattern : JapiPattern.METHOD_KEYWORD) {
                    Matcher matcher = methodPattern.matcher(line);
                    if (matcher.find()) {//匹配到方法
                        methodBodyAndDocs.add(methodLines);
                        methodLines = null;
                        isFindDocBegin = false;
                        break;
                    }
                }
            }
        }

        return methodBodyAndDocs;
    }

    /**
     * 提取方法注释信息
     *
     * @param methodLines
     * @return
     */
    private List<IActionMethodDoc> extractDoc(final List<String> methodLines) {
        boolean methodBegin = false;
        List<IActionMethodDoc> methodDocs = new ArrayList<>();

        for (String methodLine : methodLines) {
            Matcher matcherDocBegin = JapiPattern.DOC_PATTERN_BEGIN.matcher(methodLine);
            if (!methodBegin && matcherDocBegin.find()) {
                methodBegin = true;
                continue;
            }
            if (methodBegin) {
                Matcher matcherDocEnd = JapiPattern.DOC_PATTERN_END.matcher(methodLine);
                if (matcherDocEnd.find()) {
                    break;
                }
            }
            if (methodBegin) {
                ActionMethodDocImpl docImpl = new ActionMethodDocImpl();
                Matcher methodFunDesMatcher = JapiPattern.DOC_METHOD_FUN_DES.matcher(methodLine);//方法功能描述
                if (methodFunDesMatcher.find()) {
                    Matcher methodMoreMatcher = JapiPattern.DOC_MORE.matcher(methodLine);
                    if (methodMoreMatcher.find()) {
                        docImpl.setName(methodFunDesMatcher.group().substring(methodMoreMatcher.group().length()));
                        docImpl.setDocType(DocType.FUNDES.name());
                    }
                } else {
                    Matcher methodMoreMatcher = JapiPattern.DOC_MORE.matcher(methodLine);//注释左   *
                    if (methodMoreMatcher.find()) {//   *
                        docImpl.setName(methodLine.substring(methodMoreMatcher.group().length()));
                        Matcher methodNameMatcher = JapiPattern.DOC_NAME.matcher(methodLine);//注释名称 * \@param
                        if (methodNameMatcher.find()) {
                            String methodNameValue = methodNameMatcher.group();
                            String docName = methodNameValue.substring(3);
                            docImpl.setName(docName);
                            Matcher methodNameValueMatcher = JapiPattern.DOC_NAME_VALUE.matcher(methodLine);//注释名称 * \@param user
                            String docTagDes = DocTagImpl.getInstance().getTagDesByName(docName);
                            String _docTagDes = StringUtils.isBlank(docTagDes) ? DocTagImpl.getInstance().getTagDesByName(docName + ".") : docTagDes;
                            boolean isSingleTag = StringUtils.isBlank(docTagDes) && !(StringUtils.isNotBlank(_docTagDes) && _docTagDes.equals(docTagDes));
                            if (StringUtils.isNotBlank(_docTagDes)) {//是否匹配注释tag：return.
                                docImpl.setDocType(_docTagDes);
                                String valueAndDes = methodLine.substring(methodLine.indexOf(docName) + docName.length()).trim();
                                if (!isSingleTag) {
                                    int emptySpaceIndex = valueAndDes.indexOf(StringUtils.SPACE);
                                    if (emptySpaceIndex == -1) {
                                        docImpl.setValue(valueAndDes);
                                        LOGGER.warn(methodLine.trim().substring(2) + " 没有注释信息.");
                                    } else {
                                        docImpl.setValue(StringUtils.substring(valueAndDes, 0, emptySpaceIndex));
                                        docImpl.setDes(StringUtils.substring(valueAndDes, docImpl.getValue().length()).trim());
                                    }
                                } else {
                                    docImpl.setValue(valueAndDes);
                                }

                            } else if (methodNameValueMatcher.find()) {
                                String docValueAndDes = methodNameValueMatcher.group();
                                String docValue = docValueAndDes.substring(methodNameValue.length());
                                if (methodLine.endsWith(docValue)) {
                                    String[] docTypeAndValue = docValueAndDes.substring(3).split(StringUtils.SPACE);
                                    docImpl.setDocType(docTypeAndValue[0]);
                                    docImpl.setValue(docTypeAndValue[1]);
                                } else {
                                    String docDes = methodLine.substring(methodLine.indexOf(docValue)).trim().substring(docValue.length());
                                    docImpl.setDes(docDes.trim());
                                    docImpl.setValue(docValue);
                                }
                            }
                        }
                    }
                }
                if (StringUtils.isBlank(docImpl.getName()) && StringUtils.isBlank(docImpl.getValue()) && StringUtils.isBlank(docImpl.getDes())) {
                    continue;//去掉无效的换行注释
                }
                methodDocs.add(docImpl);
            }
        }
        return methodDocs;
    }


    private IType getType(final String returnTypeStr) {
        TypeImpl returnTypeImpl = new TypeImpl();
        returnTypeImpl.setJavaFile(actionFile);
        returnTypeImpl.setJavaKeyTxt(returnTypeStr);
        return returnTypeImpl;
    }

    /**
     * 获取方法返回值
     *
     * @param methodLineStr 方法行
     * @return 类型：String
     */
    private String getMethodReturnTypeStr(final String methodLineStr) {
        String returnTypeStr = null;
        for (Pattern typePattern : JapiPattern.METHOD_RETURN_TYPES) {
            Matcher returnTypeMatch = typePattern.matcher(methodLineStr);
            if (returnTypeMatch.find()) {
                returnTypeStr = StringUtils.substring(returnTypeMatch.group(), 0, -1).trim();//public String testUser
                break;
            }
        }
        String returnType = null;
        if (StringUtils.isNotBlank(returnTypeStr)) {
            returnType = returnTypeStr.substring(returnTypeStr.indexOf(StringUtils.SPACE), returnTypeStr.lastIndexOf(StringUtils.SPACE));
            returnType = returnType.trim();
        }
        return returnType;
    }

    /**
     * 获取方法参数信息
     *
     * @param methodLineStr 方法行
     * @return 参数列表
     */
    private List<String> getMethodParameterStrs(String methodLineStr) {
        List<String> parameters = new ArrayList<>();
        String methodLine = methodLineStr;
        Pattern hasThrowsExceptionPattern = JapiPattern.getPattern("\\s*throws\\s*[a-zA-Z0-9_]*\\s*[{]");
        Matcher hasThrowsExceptionMatcher = hasThrowsExceptionPattern.matcher(methodLine);
        if (hasThrowsExceptionMatcher.find()) {
            methodLine = methodLineStr.substring(0, hasThrowsExceptionMatcher.start()) + "{";
        }
        Matcher matcher = JapiPattern.PARAMETER_BODYS.matcher(methodLine);
        if (matcher.find()) {
            String parStrs = StringUtils.substring(matcher.group(), 1, -1).trim();
            parStrs = StringUtils.substring(parStrs.trim(), 0, -1);
            Matcher parMatcher = JapiPattern.PARAMETER_SINGLE_NAME.matcher(parStrs);
            int initSearchIndex = 0;
            int lastSearchIndex = 0;
            while (parMatcher.find()) {
                lastSearchIndex = parMatcher.end();
                parameters.add(parStrs.substring(initSearchIndex, lastSearchIndex - 1).trim());
                initSearchIndex = lastSearchIndex;
            }
            if (lastSearchIndex < parStrs.length()) {
                parameters.add(parStrs.substring(lastSearchIndex).trim());
            }
        }
        return parameters;
    }

    /**
     * 提取方法信息
     *
     * @param methodLines 方法doc及注解及行信息
     * @return 方法信息
     */
    private MethodImpl extractMethod(List<String> methodLines, List<String> annoLines) {
        MethodImpl methodImpl = new MethodImpl();
        List<String> annotationStrs = new ArrayList<>();
        List<String> docsStrs = new ArrayList<>();
        String methodLineStr = null;
        boolean annoBegin = false;
        for (String methodLine : methodLines) {
            Matcher matcherDocEnd = JapiPattern.DOC_PATTERN_END.matcher(methodLine);
            if (!annoBegin && matcherDocEnd.find()) {
                annoBegin = true;
                continue;
            }
            if (annoBegin) {
                Matcher annotationMatcher = JapiPattern.ANNOTATION_PATTERN.matcher(methodLine);
                if (annotationMatcher.find()) {//注解
                    annotationStrs.add(methodLine.trim().substring(1));
                } else {//方法
                    methodLineStr = methodLine.trim();
                }
            } else {
                docsStrs.add(methodLine);
            }
        }
        docsStrs.remove(0);//remove /**
        String returnTypeStr = getMethodReturnTypeStr(methodLineStr);
        IType type = getType(returnTypeStr);
        ActionRequest actionRequest = getRequestsByAnnotations(annotationStrs, annoLines);

        List<String> methodParameterStrs = getMethodParameterStrs(methodLineStr);
        List<IParameter> parameters = getMethodParameter(methodParameterStrs, docsStrs);
        methodImpl.setType(type);
        methodImpl.setAnnotations(annotationStrs);
        methodImpl.setRequest(actionRequest);
        methodImpl.setParameters(parameters);

        return methodImpl;
    }

    private List<IValid> getImvcs() {
        List<IValid> imvcs = new ArrayList<>();
        MVCValid mvcValid = new MVCValid();
        mvcValid.setJavaFilePath(actionFile.getAbsolutePath());
        imvcs.add(mvcValid);

        JSR303Valid jsr303Valid = new JSR303Valid();
        jsr303Valid.setJavaFilePath(actionFile.getAbsolutePath());
        imvcs.add(jsr303Valid);
        return imvcs;
    }

    /**
     * 将参数字符串转为参数对象
     *
     * @param methodParameterStrs
     * @return 参数对象列表
     */
    private List<IParameter> getMethodParameter(List<String> methodParameterStrs, List<String> docsStrs) {
        List<IValid> imvcs = getImvcs();
        List<IParameter> parameterList = new ArrayList<>();
        DefaultValid defaultValid = new DefaultValid();
        defaultValid.setJavaFilePath(actionFile.getAbsolutePath());

        for (String parameterStr : methodParameterStrs) {
            if (checkHasAnno(parameterStr)) {//包含注解 @RequestParam Integer cc
                Matcher annotationMatcher = JapiPattern.ANNOTATION.matcher(parameterStr);
                if (annotationMatcher.find()) {//查找注解
                    String annoStr = annotationMatcher.group();
                    for (IValid valid : imvcs) {
                        if (valid.isValid(annoStr)) {
                            IParameter iParameter = valid.getParameter(parameterStr, docsStrs);
                            if (null != iParameter) {
                                parameterList.add(iParameter);
                            }
                        }
                    }
                }
            } else {//不包含,看是否在action所排除的字段内
                String type = parameterStr.split(" ")[0];
                if (!BuiltInActionImpl.getInstance().isBuiltInType(type)) {
                    parameterList.add(defaultValid.getParameter(parameterStr, docsStrs));
                }
            }
        }
        return parameterList;
    }

    private boolean checkHasAnno(String parameter) {
        return parameter.startsWith("@");
    }

    /**
     * 从注解中获取请求url地扯
     *
     * @param annotationStrs
     * @return
     */
    private ActionRequest getRequestsByAnnotations(List<String> annotationStrs, List<String> annoLines) {
        List<String> requestAnnos = new ArrayList<>();
        List<String> requestAnnosOrigin = new ArrayList<>();

        for (String annotationLine : annotationStrs) {
            Matcher requestAnnoLeftMatcher = JapiPattern.REQUEST_ANNO_LEFT_PATTERN.matcher(annotationLine);
            Matcher requestAnnoMatcher = JapiPattern.REQUEST_ANNO_PATTERN.matcher(annotationLine);
            if (requestAnnoLeftMatcher.find()) {
                requestAnnosOrigin.add(annotationLine);
                requestAnnos.add(StringUtils.substring(requestAnnoLeftMatcher.group(), 0, -1));
            } else if (requestAnnoMatcher.find()) {
                requestAnnosOrigin.add(annotationLine);
                requestAnnos.add(StringUtils.substring(requestAnnoMatcher.group(), 0, -1));
            }
        }

        IActionRequest actionRequest = null;
        String requestAnnoOrign = null, requestAnno = null;
        if (requestAnnos.size() > 0) {
            for (int index = 0, len = requestAnnos.size(); index < len; index++) {
                String _requestAnno = requestAnnos.get(index);
                for (IActionRequest ar : MVCActionRequest.getMVCActionRequest()) {
                    if (ar.annotation().equals(_requestAnno)) {
                        actionRequest = ar;
                        requestAnno = _requestAnno;
                        requestAnnoOrign = requestAnnosOrigin.get(index);
                        break;
                    } else if (ar.annotation().indexOf(".") >= -1) {
                        if (ar.annotation().endsWith(_requestAnno)) {
                            actionRequest = ar;
                            requestAnno = _requestAnno;
                            requestAnnoOrign = requestAnnosOrigin.get(index);
                            break;
                        }
                    }
                }
            }

        }
        List<String> requestUrls = new ArrayList<>();
        RequestMethod[] methodTypeList = null;
        if (null != actionRequest) {
            Pattern pattern = JapiPattern.getPattern(actionRequest.valueField() + "(\\s)*[=](\\s)*");
            Matcher matcher = pattern.matcher(requestAnnoOrign);
            if (matcher.find()) {
                String arryOrSingle = matcher.group();
                String beginStr = StringUtils.substring(requestAnnoOrign, matcher.start());
                if (beginStr.startsWith(arryOrSingle + "\"")) {//单个值
                    String valueAndEndSym = beginStr.substring(arryOrSingle.length());
                    requestUrls.add(StringUtils.substring(valueAndEndSym, 1, valueAndEndSym.indexOf("\"", 2)));
                } else if (beginStr.startsWith(arryOrSingle + "{")) {//多个值
                    Matcher symBeginMatcher = JapiPattern.PATTERN_SYM_BEGIN.matcher(beginStr);
                    Matcher symEndMatcher = JapiPattern.PATTERN_SYM_END.matcher(beginStr);
                    if (symBeginMatcher.find() && symEndMatcher.find()) {
                        String arrStr = StringUtils.substring(beginStr, symBeginMatcher.start() + 1, symEndMatcher.end() - 1).trim();
                        requestUrls.addAll(Arrays.asList(arrStr.split(",")));
                    }
                }
            } else {
                String symAndValue = StringUtils.substring(requestAnnoOrign, requestAnno.length());
                requestUrls.add(StringUtils.substring(symAndValue.trim(), 2, -2));
            }

            if (actionRequest.getMethod().equals(RequestMethod.ALL)) {//未知请求,找定义方法字段
                if (StringUtils.isBlank(actionRequest.methodField())) {
                    String errMsg = "actionRequest methodField 未知请求的字段不能为空";
                    LOGGER.error(errMsg);
                    throw new JapiException(errMsg);
                }
                Pattern methodPattern = JapiPattern.getPattern(actionRequest.methodField() + "(\\s)*[=](\\s)*");
                Matcher methodMatcher = methodPattern.matcher(requestAnnoOrign);
                if (methodMatcher.find()) {
                    Pattern multiMethodPattern = JapiPattern.getPattern(actionRequest.methodField() + "(\\s)*[=](\\s)*[{]\\S*[}]");//多个请求方式
                    Matcher multiMethodMatcher = multiMethodPattern.matcher(requestAnnoOrign);
                    if (multiMethodMatcher.find()) {
                        String arrMethod = multiMethodMatcher.group();
                        arrMethod = StringUtils.substring(arrMethod, arrMethod.indexOf("{") + 1, arrMethod.lastIndexOf("}"));
                        String[] arrMethods = arrMethod.split(",");
                        methodTypeList = new RequestMethod[arrMethods.length];
                        int methodTypeListIndex = 0;
                        for (String[] methodType : actionRequest.methodValues()) {
                            for (String arrMethodStr : arrMethods) {
                                if (methodType[0].endsWith(arrMethodStr)) {
                                    methodTypeList[methodTypeListIndex++] = RequestMethod.match(methodType[1]);
                                }
                            }
                        }
                    } else {
                        Pattern singleMethodPattern = JapiPattern.getPattern(actionRequest.methodField() + "(\\s)*[=](\\s)*\\S*");//单个请求方式
                        Matcher singleMethodMatcher = singleMethodPattern.matcher(requestAnnoOrign);
                        if (singleMethodMatcher.find()) {
                            String matchStr = singleMethodMatcher.group();
                            String spectorStr = null;
                            if (matchStr.indexOf(",") != -1) {
                                spectorStr = ",";
                            } else {
                                spectorStr = ")";
                            }
                            String singleMethodStr = matchStr.substring(matchStr.indexOf("=") + 1, matchStr.lastIndexOf(spectorStr)).trim();
                            methodTypeList = new RequestMethod[1];
                            for (String[] methodType : actionRequest.methodValues()) {
                                if (methodType[0].endsWith(singleMethodStr)) {
                                    methodTypeList[0] = RequestMethod.match(methodType[1]);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        List<String> javaUrls = new ArrayList<>();
        for (String anno : annoLines) {
            Pattern requestMapping = JapiPattern.getPattern("@RequestMapping\\s*[(]\\s*[\"][a-zA-Z0-9_/{}]*[\"]\\s*[)]");
            Matcher requestMatcher = requestMapping.matcher(anno);
            if (requestMatcher.find()) {
                String annoAndUrl = requestMatcher.group();
                javaUrls.add(annoAndUrl.substring(annoAndUrl.indexOf("\"") + 1, annoAndUrl.lastIndexOf("\"")));
            } else {
                Pattern requestMappingPatternPathAndValue = JapiPattern.getPattern("(path|value)\\s*[=]\\s*[{]\\s*[a-zA-Z0-9_/{}\",]*\\s*[}]");//path = {"asdf/main"} || value = {"asdf/main","asdf"}
                Matcher requestMappingMatcherPathAndValue = requestMappingPatternPathAndValue.matcher(anno);
                if (requestMappingMatcherPathAndValue.find()) {
                    String mappingStrs = requestMappingMatcherPathAndValue.group();
                    String[] urls = mappingStrs.substring(mappingStrs.indexOf("{") + 1, mappingStrs.lastIndexOf("}")).split(",");
                    for (String url : urls) {
                        javaUrls.add(url.substring(url.indexOf("\"") + 1, url.lastIndexOf("\"")));
                    }
                }
            }

        }
        List<String> newRequests = new ArrayList<>();
        if (null != javaUrls && javaUrls.size() > 0) {
            for (String requestUrl : requestUrls) {
                for (String javaUrl : javaUrls) {
                    newRequests.add(javaUrl + "/" + requestUrl);
                }
            }
        } else {
            newRequests = requestUrls;
        }
        return new ActionRequest(newRequests, (null != methodTypeList && methodTypeList.length > 0) ? Arrays.asList(methodTypeList) : Arrays.asList(new RequestMethod[]{actionRequest.getMethod()}));
    }

    public List<ActionInfo> getActionInfos(List<IActionMethod> actionMethods) {
        List<ActionInfo> actionInfos = new ArrayList<>();
        Pattern resutlFunPattern = JapiPattern.getPattern("[{]\\s*[a-zA-Z0-9_]*\\s*[:]\\s*");
        for (IActionMethod actionMethod : actionMethods) {
            ActionInfo actionInfo = new ActionInfo();
            String version = null;
            actionInfo.setActionName(actionMethod.getMethodDescription());
            List<IRequest> requestFields = new ArrayList<>();//TODO
            for (IParameter parameter : actionMethod.getParameters()) {
                requestFields.addAll(parameter.getRequestFields());//TODO
            }
            ActionInfoRequest actionInfoRequest = new ActionInfoRequest();
            for(String url : actionMethod.getRequest().getUrls()){
                if(!url.matches("^([a-z{]+[a-z0-9-A-Z{}]*/*)*$")){
                    throw new JapiException(url+" 请求URL不符合RESTFul命名规则，请检查.");
                }
            }
            actionInfoRequest.setUrls(actionMethod.getRequest().getUrls());
            actionInfoRequest.setMethods(actionMethod.getRequest().getMethods());
            actionInfo.setActionInfoRequest(actionInfoRequest);
            actionInfo.setRequestFields(requestFields);//TODO
            boolean hasReturnDoc = false;
            boolean parserReturnDoc = false;
            for (IActionMethodDoc doc : actionMethod.getDocs()) {
                if (!hasReturnDoc && doc.getName().equals("return")) {
                    hasReturnDoc = true;
                }
                if (doc.getName().equals("version")) {
                    if (null == version) {
                        actionInfo.setVersion(doc.getValue());
                    }
                }

                if (hasReturnDoc && !parserReturnDoc) {
                    parserReturnDoc = true;
                    Matcher restfulMatcher = resutlFunPattern.matcher(doc.getValue());
                    if (restfulMatcher.find()) {
                        if (doc.getValue().trim().startsWith("[")) {//数组
                            List<ResponseImpl> responses = JSON.parseArray(doc.getValue(), ResponseImpl.class);
                            if (null != actionInfo.getResponseFields()) {
                                actionInfo.getResponseFields().addAll(responses);
                            } else {
                                actionInfo.setResponseFields((List) responses);
                            }
                        } else if (doc.getValue().trim().startsWith("{")) {
                            IResponse response = JSON.parseObject(doc.getValue(), ResponseImpl.class);
                            if (null != actionInfo.getResponseFields()) {
                                actionInfo.getResponseFields().add(response);
                            } else {
                                actionInfo.setResponseFields(Arrays.asList(response));
                            }
                        }
                    } else if (doc.getValue().split(" ")[0].equals("class")) {
                        if(doc.getValue().split(" ").length==1){
                            throw new JapiException("@return class 后面请接上实体类.");
                        }
                        IType returnType = getType(doc.getValue().split(" ")[1]);
                        if (null == actionInfo.getResponseFields()) {
                            actionInfo.setResponseFields(getChildFields(returnType.getFields()));
                        } else {
                            actionInfo.getResponseFields().addAll(getChildFields(returnType.getFields()));
                        }
                    } else if (StringUtils.isBlank(doc.getValue())) {
                        throw new JapiException("方法[ " + actionInfo.getActionName() + " ] @return 注释不能为空.");
                    } else {
                        throw new JapiException("方法[ " + actionInfo.getActionName() + " ] @return " + doc.getValue() + " 注释不符合规范,请检查.");
                    }

                } else {
                    if (!ExcludesActionImpl.getInstance().isExcludesTag(doc.getName())) {
                        ActionInfoDoc actionInfoDoc = new ActionInfoDoc();
                        if(StringUtils.isBlank(doc.getDocType())){
                            actionInfoDoc.setTagName(doc.getName());
                            actionInfoDoc.setTagValue(doc.getValue()+" "+doc.getDes()==null?"":doc.getDes());
                        }else{
                            actionInfoDoc.setTagName(doc.getDocType());
                            actionInfoDoc.setTagValue(doc.getValue());
                        }
                        actionInfo.getActionInfoDocs().add(actionInfoDoc);
                    }
                }
            }
            if (!hasReturnDoc) {//use action return default type

                if (BuiltInJavaImpl.getInstance().isBuiltInType(actionMethod.getType().getName())) {
                    throw new JapiException("方法[ " + actionMethod.getMethodDescription() + " ] 反回值不支持java内置对象,请检查.");
                } else if ("void".equals(actionMethod.getType().getName())) {
                    throw new JapiException("方法[ " + actionMethod.getMethodDescription() + " ] 没有反回值,注释也没有标注@return,请检查.");
                } else {
                    List<IResponse> responses = getChildFields(actionMethod.getType().getFields());
                    if (null != responses && responses.size() > 0) {
                        if (null == actionInfo.getResponseFields()) {
                            actionInfo.setResponseFields(responses);
                        } else {
                            actionInfo.getResponseFields().addAll(responses);
                        }
                    }
                }

            }
            if (StringUtils.isBlank(actionInfo.getVersion())) {
                throw new JapiException("方法[ " + actionInfo.getActionName() + " ] 版本号不能为空");
            } else {
                if (!actionInfo.getVersion().matches("v\\d+")) {
                    throw new JapiException("方法[ " + actionInfo.getVersion() + " ] 版本号不符合规范");
                }
            }
            if (StringUtils.isBlank(actionInfo.getActionName())) {
                throw new JapiException("请求方法名不能为空");
            }
            actionInfos.add(actionInfo);
        }

        return actionInfos;
    }

    @Override
    public String getName() {
        try {
            List<String> javaLines = FileUtils.readLines(actionFile, Charset.forName("utf-8"));
            Pattern docBeginPattern = JapiPattern.getPattern("[/][*][*]");
            Pattern classBeginPattern = JapiPattern.getPattern("[a-zA-Z0-9_]+\\s*[{]$");
            List<String> docsAndAnnos = new ArrayList<>();
            boolean docBegin = false;
            boolean classBegin = false;
            for (String line : javaLines) {
                if (false == docBegin && docBeginPattern.matcher(line).find()) {
                    docBegin = true;
                }
                if (docBegin && !classBegin) {
                    docsAndAnnos.add(line);
                }
                if (classBeginPattern.matcher(line).find()) {
                    classBegin = true;
                    break;
                }
            }
            if (docsAndAnnos.size() > 0) {
                Pattern docEndPattern = JapiPattern.getPattern("[*][/]$");
                List<String> docs = new ArrayList<>();
                for (String line : docsAndAnnos) {
                    docs.add(line);
                    if (docEndPattern.matcher(line).find()) {
                        break;
                    }
                }
                String name = "";
                for (String line : docs) {
                    if (line.length() > 3) {
                        name = line.substring(3).trim();
                        break;
                    }
                }
                if (name.contains("/") || name.contains(" ") || name.contains(",")) {
                    throw new JapiException("类名注释[ " + name + " ] 不能有特殊符号['/',' ',',']");
                }
                return name;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提取方法信息
     *
     * @return 方法列表
     */
    private List<IActionMethod> extractDocAndMethodInfo(final List<List<String>> methodBodyAndDocs, List<String> annoLines) {
        List<IActionMethod> methodImpls = new ArrayList<>(methodBodyAndDocs.size());
        for (List<String> methodLines : methodBodyAndDocs) {
            MethodImpl methodImpl = new MethodImpl();

            List<IActionMethodDoc> methodDocs = extractDoc(methodLines);//提取方法注释信息
            MethodImpl extractMethod = extractMethod(methodLines, annoLines);//提取方法信息
            Iterator<IActionMethodDoc> methodDocIterator = methodDocs.iterator();
            while (methodDocIterator.hasNext()) {//提取方法描述信息
                IActionMethodDoc actionMethodDoc = methodDocIterator.next();
                if (DocType.FUNDES.name().equals(actionMethodDoc.getDocType())) {
                    methodImpl.setMethodDescription(actionMethodDoc.getName());
                    methodDocIterator.remove();
                    break;
                }
            }
            methodImpl.setDocs(methodDocs);
            methodImpl.setAnnotations(extractMethod.getAnnotations());
            methodImpl.setType(extractMethod.getType());
            methodImpl.setRequest(extractMethod.getRequest());
            methodImpl.setParameters(extractMethod.getParameters());

            methodImpls.add(methodImpl);
        }
        return methodImpls;
    }

    private List<IResponse> getChildFields(List<IField> iFields) {
        List<IResponse> responses = new ArrayList<>();
        for (IField iField : iFields) {
            ResponseImpl response = new ResponseImpl();
            String description = "";
            if(!iField.getName().matches("^[a-z]+[a-z0-9A-Z]*")){
                throw new JapiException(iField.getName()+" 不符号RESTFul字段命名规范，请检查.");
            }
            response.setName(iField.getName());

            for (IFieldDoc fieldDoc : iField.getDocs()) {
                if (StringUtils.isBlank(fieldDoc.getValue())) {
                    description = fieldDoc.getName();
                    break;
                }
            }
            response.setDescription(description);

            if(null!=iField.getFields()&&iField.getFields().size()==1&&iField.getFields().get(0).isEnumType()){
                IRequest request = iField.getFields().get(0).enumRequest();
                response.setType("string");
                if(null!=response.getDescription()){
                    response.setDescription(response.getDescription()+" { "+request.getConstraint()+" } ");
                }else{
                    response.setDescription(request.getConstraint());
                }
            }else{
                boolean isArr= iField.getType().startsWith("array ");
                String arrStr = isArr?"[]":"";
                if (!"$this".equals(iField.getType())&&!"$this[]".equals(iField.getType())) {
                    response.setType(TypeConvert.getHtmlType(isArr?iField.getType().substring(6):iField.getType()));
                } else {
                    response.setType(isArr?iField.getType().substring(6):iField.getType());
                }
                response.setType(response.getType()+arrStr);
                response.setDefaultValue("");
                if (iField.getFields() != null) {
                    response.setFields(getChildFields(iField.getFields()));
                }
            }
            responses.add(response);
        }
        return responses;
    }


    public File getActionFile() {
        return actionFile;
    }

    public void setActionFile(File actionFile) {
        this.actionFile = actionFile;
    }

}
