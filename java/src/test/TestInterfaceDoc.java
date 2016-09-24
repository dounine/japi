/**
 * Created by ike on 16-9-20.
 */

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.junit.Test;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ike on 16-9-14.
 */
public class TestInterfaceDoc {
    private ClassLoader classLoader;
    private List<Map<String, Object>> halist =null;
    private String classAn = null;
    private String packageInfo = null;

    @Test
    public void test2() {
        apiMain();
    }

    public List<Object> apiMain() {
        String entityPrePath = "com.dounine.japi.web";
        String filePath = "/home/ike/java/java/japi/java/src/main/java/com/dounine/japi/web";
//        String entityPrePath = "dnn.web";
//        String filePath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web";
        File file = new File(filePath);
        String[] names = file.list();

        List<Object> listActName = new ArrayList<Object>();

        List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
        halist =new ArrayList<>();
        halist = dirToName(filePath, entityPrePath, names, null);
        for (Map<String, Object> maps : halist) {
            String filePaths = maps.get("filePath").toString();
            String entityPrePaths = maps.get("entityPrePath").toString();
            String classname = maps.get("classname").toString();
            String dir = null;
            if (maps.get("dir") != null) {
                dir = maps.get("dir").toString();
            }
            classList = webActName(filePaths, entityPrePaths, classname, dir);
            listActName.add(classList);
        }
        System.out.println("吃饭hi额外" + listActName);


        return listActName;
    }

    public List<Map<String, Object>> dirToName(String filePath, String entityPrePath, String[] names, String dir) {
        String filePathName = "";
        for (String s : names) {
            Map<String, Object> map = new HashMap<>();
            if (s.trim().equals("InterfaceDoc.java")) {
                continue;
            }
            if (s.trim().equals("package-info.java")) {
                continue;
            }
            //直接是目录
            if (!s.contains(".java")) {
                filePathName = filePath + "/" + s;
                File fileDir = new File(filePathName);// /w
                if (dir != null) {
                    s = dir + "." + s;
                }
                if (fileDir.isDirectory()) {
                    String[] fileDirNames = fileDir.list();
                    for (int i = 0; i < fileDirNames.length; i++) {
                        dirToName(filePathName, entityPrePath, fileDirNames, s);
                        break;
                    }
                    continue;
                }
                continue;
            }
            map.put("filePath", filePath);
            map.put("entityPrePath", entityPrePath);
            map.put("classname", s);
            map.put("dir", dir);
            halist.add(map);
        }
        return halist;
    }

    public List<Map<String, Object>> anno(String filePath, String classname) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        try {
            List<String> readDocList = readDocByStream(filePath, classname);
            for (String explain : readDocList) {
                Map<String, Object> map = new HashMap<String, Object>();
                String[] classSplit = explain.split("class\\s*" + classname + "\\s*\\{");
                String classSplitLeft = null;
                if (classSplit.length >= 2) {
                    explain = classSplit[1];
                    classSplitLeft = classSplit[0];
                }
                if (classSplitLeft != null) {
                    classAn = classAnnotations(classSplitLeft);

                }
                String[] splits = explain.split("public");
                if (splits != null && splits.length >= 2) {
                    String returnAndParams = splits[1];
                    String[] splitMethods = returnAndParams.split("\\(");
                    if (splitMethods != null) {
                        String returnMethod = "";
                        returnMethod = splitMethods[0];//String ld:返回值+方法名
                        String[] methodNames = returnMethod.split("\\s");
                        if (methodNames != null) {
                            String methodName = "";
                            methodName = methodNames[2];//ld:方法名
                            map.put("methodName", methodName);
                        }
                    }
                }

                //拿到注释
                if (map.get("methodName") == null) {
                    continue;
                }
                if (splits != null) {
                    String annotation = splits[0];
                    String[] sqr = annotation.split("\\*\\s*#");
                    for (String explainSplit : sqr) {
                        if (explainSplit.contains("/**")) {
                            String ss[] = explainSplit.split("/\\**");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                descs = ss[1].replace("    *", "").trim();
                            } else {
                                descs = "";
                            }
                            if (map.get("desc") != null) {
                                map.put("desc", map.get("desc") + "," + descs);
                            } else {
                                map.put("desc", descs);
                            }
                        } else if (explainSplit.startsWith("demo")) {
                            map = annotationSplit(explainSplit, map, "demo", "demoUrl");
                        } else if (explainSplit.startsWith("exclude")) {
                            map = annotationSplit(explainSplit, map, "exclude", "paramExclude");
                        } else if (explainSplit.startsWith("include")) {
                            map = annotationSplit(explainSplit, map, "include", "paramInclude");
                        } else if (explainSplit.startsWith("return")) {
                            map = annotationSplit(explainSplit, map, "return", "return");
                        }
                    }
                }
                if (map.size() > 0) {
                    listMap.add(map);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listMap;
    }

    public List<Map<String, Object>> webActName(String filePath, String entityPrePath, String s, String dir) {
        String filePathName = "";

        //直接是文件.java
        String[] strings = s.split(".java");
        Class<?> demo1 = null;
        List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> getAnnoInfo = anno(filePath, strings[0]);//解析注释

        try {
            if (dir != null) {
//                    demo1 = Class.forName("com.dounine.japi.web."+dir+"."+ strings[0]);
                demo1 = Class.forName(entityPrePath + "." + dir + "." + strings[0]);
                packageInfo = readPackageInfo(filePath, "package-info");
                if (packageInfo == null) {
                    packageInfo = entityPrePath + "." + dir + "包";
                }
            } else {
//                    demo1 = Class.forName("com.dounine.japi.web." + strings[0]);
                demo1 = Class.forName(entityPrePath + "." + strings[0]);
                packageInfo = readPackageInfo(filePath, "package-info");
                if (packageInfo == null) {
                    packageInfo = entityPrePath + "包";
                }
            }
            Method[] dan = demo1.getMethods();//获取所有方法
            for (Method method : dan) {
                String methodName = method.getName();
                switch (methodName) {
                    case "wait":
                        continue;
                    case "equals":
                        continue;
                    case "toString":
                        continue;
                    case "hashCode":
                        continue;
                    case "getClass":
                        continue;
                    case "notify":
                        continue;
                    case "notifyAll":
                        continue;
                }
                Map<String, Object> mapMethodParams = new HashMap<String, Object>(2);
                mapMethodParams = writeCheckStatus(getAnnoInfo, mapMethodParams);

                Type[] type = method.getGenericParameterTypes();
                List<Object> parmType = new ArrayList<Object>(1);
                for (Type ty : type) {
                    String pty = ty + "";
                    if (pty.contains("[")) {
                        int left = pty.indexOf("[");
                        pty = pty.substring(0, left) + pty.substring(left + 1);
                    }
                    parmType.add(pty);
                    mapMethodParams = paramTypeAndName(demo1, method, mapMethodParams);
                    mapMethodParams = paramEntityAttr(entityPrePath, ty, mapMethodParams);
                }
                mapMethodParams.put("class", s);
                mapMethodParams.put("methodName", method.getName());
                mapMethodParams.put("returnType", method.getReturnType().getName());
                mapMethodParams.put("paramType", parmType);

                MethodRequsetDeal(demo1, method, mapMethodParams);
                mapMethodParams = mapMethodParamsAdd(method, getAnnoInfo, mapMethodParams);

                if (mapMethodParams.get("paramsValue") != null) {
                    if (mapMethodParams.get("paramInclude") != null && !mapMethodParams.get("paramInclude").equals("")) {
                        mapMethodParams = paramInclude(mapMethodParams, "paramInclude");
                    } else if ((mapMethodParams.get("paramInclude") == null || mapMethodParams.get("paramInclude").equals("")) && mapMethodParams.get("paramExclude") != null) {
                        mapMethodParams = paramInclude(mapMethodParams, "paramExclude");
                    } else {
                        if (mapMethodParams.get("paramsValue") != null) {
                            String paramsValue = mapMethodParams.get("paramsValue").toString().trim();
                            mapMethodParams.put("paramTypeList", paramsValue);
                        }
                    }
                }
                if (classAn == null) {
                    String[] classNameSplit = mapMethodParams.get("class").toString().split(".java");
                    mapMethodParams.put("classDes", classNameSplit[0]);
                } else {
                    mapMethodParams.put("classDes", classAn);
                }
                mapMethodParams.put("packageInfo", packageInfo);
//                    mapMethodParams.remove("paramType");
//                    mapMethodParams.remove("paramsValue");
                classList.add(mapMethodParams);
            }
            System.out.println("秒:" + classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    public void MethodRequsetDeal(Class<?> demo1, Method method, Map<String, Object> mapMethodParams) {
        Annotation[] actAnnos = demo1.getDeclaredAnnotations();
        String[] rmVlues = null;
        for (Annotation annotation : actAnnos) {
            String annoSimpleName = annotation.annotationType().getSimpleName();
            if ("RequestMapping".equals(annoSimpleName)) {
                rmVlues = demo1.getDeclaredAnnotation(RequestMapping.class).value();
            }
        }
        Annotation[] actMethodAnnos = method.getDeclaredAnnotations();
        for (Annotation annotation : actMethodAnnos) {
            String annoSimpleName = annotation.annotationType().getSimpleName();
            demoUrl(method, annoSimpleName, rmVlues, mapMethodParams);
        }
    }

    public void demoUrl(Method method, String reqMethod, String[] rmVlues, Map<String, Object> mapMethodParams) {
        String[] methodValues = null;
        RequestMethod[] reqMethods = null;
        String[] reqParams = null;
        String reqStr = "";
        switch (reqMethod) {
            case "RequestMapping":
                methodValues = method.getDeclaredAnnotation(RequestMapping.class).value();
                reqMethods = method.getDeclaredAnnotation(RequestMapping.class).method();
                reqParams = method.getDeclaredAnnotation(RequestMapping.class).params();
                reqStr = "REQUEST";
                break;
            case "PostMapping":
                methodValues = method.getDeclaredAnnotation(PostMapping.class).value();
                reqParams = method.getDeclaredAnnotation(PostMapping.class).params();
                reqStr = "POST";
                break;
            case "GetMapping":
                methodValues = method.getDeclaredAnnotation(GetMapping.class).value();
                reqParams = method.getDeclaredAnnotation(GetMapping.class).params();
                reqStr = "GET";
                break;
            case "PutMapping":
                methodValues = method.getDeclaredAnnotation(PutMapping.class).value();
                reqParams = method.getDeclaredAnnotation(PutMapping.class).params();
                reqStr = "PUT";
                break;
            case "PatchMapping":
                methodValues = method.getDeclaredAnnotation(PatchMapping.class).value();
                reqParams = method.getDeclaredAnnotation(PatchMapping.class).params();
                reqStr = "PATCH";
                break;
            case "DeleteMapping":
                methodValues = method.getDeclaredAnnotation(DeleteMapping.class).value();
                reqParams = method.getDeclaredAnnotation(DeleteMapping.class).params();
                reqStr = "DELETE";
                break;
        }

        List<String> annoMap = new ArrayList<>();
        if (methodValues != null) {
            for (int i = 0; i < methodValues.length; i++) {
                String urlStr = "http://localhost:8080";
                if (reqStr.equals("REQUEST") && reqMethods != null && reqMethods.length > 0) {
                    urlStr = reqMethods[0] + " -> " + urlStr;
                }
                if (!reqStr.equals("REQUEST")) {
                    urlStr = reqStr + " -> " + urlStr;
                }
                if (rmVlues != null && rmVlues.length > 0) {
                    urlStr = urlStr + "/" + rmVlues[0] + "/";
                }
                urlStr = urlStr + methodValues[i];
                if (reqParams != null && reqParams.length > 0) {
                    urlStr = urlStr + " -> " + reqParams[i];
                }
                annoMap.add(urlStr);
            }
            mapMethodParams.put("demo", annoMap);
        }
    }

    public Map<String, Object> writeCheckStatus(List<Map<String, Object>> getAnnoInfo, Map<String, Object> mapMethodParams) {
        if (getAnnoInfo.size() <= 0) {
            mapMethodParams.put("writeCheckStatus", "noAnnotations");
            mapMethodParams.put("writeCheckStatusDes", "该方法没有写注释");
        } else {
            for (int i = 0; i < getAnnoInfo.size(); i++) {
                if (getAnnoInfo.get(i).get("paramExclude") == null) {
                    mapMethodParams.put("writeCheckStatus", "noAnnotationsForParamExclude");
                    mapMethodParams.put("writeCheckStatusDes", "该方法的注释没写排除的参数");
                } else if (getAnnoInfo.get(i).get("paramInclude") == null) {
                    mapMethodParams.put("writeCheckStatus", "noAnnotationsForParamInclude");
                    mapMethodParams.put("writeCheckStatusDes", "该方法的注释没写加入的参数");
                } else if (getAnnoInfo.get(i).get("return") == null) {
                    mapMethodParams.put("writeCheckStatus", "noAnnotationsForReturn");
                    mapMethodParams.put("writeCheckStatusDes", "该方法的注释没写描述返回信息");
                } else if (getAnnoInfo.get(i).get("desc") == null) {
                    mapMethodParams.put("writeCheckStatus", "noAnnotationsForDesc");
                    mapMethodParams.put("writeCheckStatusDes", "该方法的注释没写方法描述信息");
                }

            }
        }
        return mapMethodParams;
    }

    public Map<String, Object> paramTypeAndName(Class<?> demo1, Method method, Map<String, Object> mapMethodParams) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(demo1);
        pool.insertClassPath(classPath);

//        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(demo1.getName());
        CtMethod cm = cc.getDeclaredMethod(method.getName());
        Type[] parameterTypes = method.getGenericParameterTypes();
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute != null) {
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            String[] paramsValue = new String[cm.getParameterTypes().length];
            List<Object> paramsValueStr = new ArrayList<Object>();
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramsValue.length; i++) {
                paramsValue[i] = attr.variableName(i + pos);
                String parameterTypesAndName = parameterTypes[i] + " " + attr.variableName(i + pos);
                if (parameterTypesAndName.contains("[")) {
                    int left = parameterTypesAndName.indexOf("[");
                    parameterTypesAndName = parameterTypesAndName.substring(0, left) + parameterTypesAndName.substring(left + 1);
                }
                paramsValueStr.add(parameterTypesAndName.replaceAll(","," "));
            }
            mapMethodParams.put("paramsValue", paramsValueStr);
        }
        return mapMethodParams;
    }

    public Map<String, Object> paramEntityAttr(String entityPrePath, Type ty, Map<String, Object> mapMethodParams) throws Exception {
        List<Map<String, Object>> listAddParams = new ArrayList<Map<String, Object>>(4);
        //有封装
        String[] parms = ty.toString().split("class ");//对象参数
        if (parms != null && parms.length > 1) {
            //class com.dounine.japi.Entity
//                            String[] paramTypeCheckObject = parms[1].split("com");//实体前缀
            String[] changePakageFirst = entityPrePath.split("\\.");
            String[] paramTypeCheckObject = parms[1].split(changePakageFirst[0]);//实体前缀
            if (paramTypeCheckObject != null && paramTypeCheckObject.length > 1) {
                Field[] fields = Class.forName(changePakageFirst[0] + paramTypeCheckObject[1]).getDeclaredFields();
                for (Field field : fields) {
                    Map<String, Object> map = new HashMap<String, Object>(1);
                    map.put("attributeName", field.getName());
                    Annotation[] annos = field.getAnnotations();
                    List<Object> listAttr = new ArrayList<>();
                    for (Annotation annotation : annos) {
                        listAttr.add(annotation);
                    }
                    map.put("attributeDetail", listAttr);
                    listAddParams.add(map);
                }
                mapMethodParams.put("entityParamAttr", listAddParams);
            }

        }
        //无封装  int
        return mapMethodParams;
    }

    public Map<String, Object> paramInclude(Map<String, Object> mapMethodParams, String paramsInclude) {
        String paramInclude = mapMethodParams.get(paramsInclude).toString().trim();
        String[] paramIncludeArrs = paramInclude.split(" ");
        String paramsValue = mapMethodParams.get("paramsValue").toString().trim();
        String[] paramsValueAs1 = paramsValue.split("\\[");
        String[] paramsValueAs2 = paramsValueAs1[1].split("\\]");
        String[] paramsValueArrs = paramsValueAs2[0].split(",");
        List<Object> paramTypeList = new ArrayList<Object>();
        for (int k = 0; k < paramsValueArrs.length; k++) {
            boolean flag = true;
            String temp = null;
            String temps = paramsValueArrs[k];
            String[] tempSplits = temps.split("\\s+");
            String tempSplit = null;
            if (tempSplits != null && tempSplits.length > 2) {
                tempSplit = tempSplits[tempSplits.length - 1];
            } else if (tempSplits != null && tempSplits.length == 2) {
                tempSplit = tempSplits[1];
            }

            String[] tSplits = null;
            String tSplit = null;
            if (tempSplit.contains("]")) {
                tSplits = tempSplit.split("]");
                tSplit = tSplits[0];
            }
            if (tSplit == null) {
                temp = tempSplit;
            } else {
                temp = tSplit;
            }
            for (int j = 0; j < paramIncludeArrs.length; j++) {
                if (paramIncludeArrs[j].equals("*")) {
                    paramTypeList.clear();
                    if (paramsInclude.equals("paramInclude")) {
                        paramTypeList.add(paramsValue);
                    }
                    break;
                }
                if (!temp.equals(paramIncludeArrs[j])) {
                    flag = false;
                } else {
                    flag = true;
                }
                if (!flag) {
                    paramTypeList.add(temps);
                }
            }
        }
        Object paramTypeListToObject = paramTypeList;
        mapMethodParams.put("paramTypeList", paramTypeListToObject);
        return mapMethodParams;
    }

    public Map<String, Object> mapMethodParamsAdd(Method method, List<Map<String, Object>> getAnnoInfo, Map<String, Object> mapMethodParams) {
        for (int i = 0; i < getAnnoInfo.size(); i++) {
            if (method.getName().equals(getAnnoInfo.get(i).get("methodName"))) {
                mapMethodParams.put("methodName", getAnnoInfo.get(i).get("methodName"));
                mapMethodParams.put("paramExclude", getAnnoInfo.get(i).get("paramExclude"));
                mapMethodParams.put("paramInclude", getAnnoInfo.get(i).get("paramInclude"));
                mapMethodParams.put("return", getAnnoInfo.get(i).get("return"));
                mapMethodParams.put("desc", getAnnoInfo.get(i).get("desc"));
            }
        }
        return mapMethodParams;
    }

    public Map<String, Object> annotationSplit(String explainSplit, Map<String, Object> map, String StrSplit, String mapStr) {
        String ss[] = explainSplit.split(StrSplit);
        String descs = "";
        if (ss != null && ss.length >= 2) {
            String[] returnStr = ss[1].split("\\**/");
            if (returnStr != null && returnStr.length > 0) {
                descs = returnStr[0].trim();
            }
        } else {
            descs = "";
        }
        if (map.get(mapStr) != null) {
            map.put(mapStr, map.get(mapStr) + " " + descs);
        } else {
            map.put(mapStr, descs);
        }
        return map;
    }

    public List<String> readDocByStream(String filePath, String classname) throws FileNotFoundException, IOException {
        BufferedReader bis = new BufferedReader(new FileReader(filePath + "/" + classname + ".java"));
        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<String>();
        while (bis.read() != -1) {
            lines.add(bis.readLine());
        }
        for (String s : lines) {
            sb.append(s);
        }
        String context = sb.toString();
        Pattern leftpattern = Pattern.compile("/\\*{2}");
        Matcher leftmatcher = leftpattern.matcher(context);
//            Pattern rightpattern = Pattern.compile("\\*/[\\s\\S]*public[\\s\\S]*\\)\\p{Blank}*[\\s\\S]*\\{");
        Pattern rightpattern = Pattern.compile("\\;");
        Matcher rightmatcher = rightpattern.matcher(context);

        List<String> list = new ArrayList<String>();
        String contexts = "";

        while (leftmatcher.find() && rightmatcher.find()) {
            while (rightmatcher.start() < leftmatcher.start()) {
                rightmatcher.find();
            }
            contexts = context.substring(leftmatcher.start(), rightmatcher.start());
            list.add(contexts);
        }
        return list;
    }

    public String classAnnotations(String classSplitLeft) {
        String[] classAnnos = null;
        String[] classDesc = null;
        String str = null;
        classAnnos = classSplitLeft.split("#classDes");
        if (classAnnos != null && classAnnos.length >= 2) {
            str = classAnnos[1];
            classDesc = str.split("\\*+");
            str = classDesc[0];
        }
        return str;

    }

    public String readPackageInfo(String filePath, String classname) throws IOException {
        FileReader fileReader = null;
        List<String> list = new ArrayList<String>();
        try {
            fileReader = new FileReader(filePath + "/" + classname + ".java");

        } catch (FileNotFoundException e) {
            System.out.println("该包没写package-info描述:" + e.getMessage());
        }
        if (fileReader == null) {
            return null;
        }
        BufferedReader bis = new BufferedReader(fileReader);

        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<String>();
        while (bis.read() != -1) {
            lines.add(bis.readLine());
        }
        for (String s : lines) {
            sb.append(s);
        }
        String context = sb.toString();
        Pattern leftpattern = Pattern.compile("\\*{2}");
        Matcher leftmatcher = leftpattern.matcher(context);
        Pattern rightpattern = Pattern.compile("\\**/");
        Matcher rightmatcher = rightpattern.matcher(context);
        String contexts = "";
        while (leftmatcher.find() && rightmatcher.find()) {
            while (rightmatcher.start() < leftmatcher.start()) {
                rightmatcher.find();
            }
            contexts = context.substring(leftmatcher.start(), rightmatcher.start());
            list.add(contexts);
        }
        if (list != null && list.size() > 0) {
            String[] packages = list.get(0).split("\\#packageInfo");
            if (packages != null && packages.length > 1) {
                packages = packages[1].split("\\*+");
                return packages[0].toString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
