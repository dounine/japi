/**
 * Created by ike on 16-9-20.
 * 第一版
 */

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
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
public class TestInterfaceDoc1 {
    private ClassLoader classLoader;

    @Test
    public void test2() {
        String entityPrePath = "com.dounine.japi.web";
        String filePath = "/home/ike/java/java/japi/java/src/main/java/com/dounine/japi/web";
//        String entityPrePath = "dnn.web";
//        String filePath = "/home/ike/java/java/feedback/java/src/main/java/dnn/web";
        File file = new File(filePath);
        String[] names = file.list();

        actName(filePath, entityPrePath, names, null);

    }

    public List<Map<String, Object>> anno(String filePath, String classname) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        try {
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
            for (String explain : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                String[] classSplit = explain.split("class\\s*" + classname + "\\s*\\{");
                if (classSplit.length >= 2) {
                    explain = classSplit[1];
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
                            String ss[] = explainSplit.split("demo");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                String[] returnStr = ss[1].split("\\**/");
                                if (returnStr != null && returnStr.length > 0) {
                                    descs = returnStr[0];
                                }
                            } else {
                                descs = "";
                            }
                            if (map.get("demoUrl") != null) {
                                map.put("demoUrl", map.get("demoUrl") + "," + descs);
                            } else {
                                map.put("demoUrl", descs);
                            }
                        } else if (explainSplit.startsWith("exclude")) {
                            String ss[] = explainSplit.split("exclude");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                String[] returnStr = ss[1].split("\\**/");
                                if (returnStr != null && returnStr.length > 0) {
                                    descs = returnStr[0];
                                }
                            } else {
                                descs = "";
                            }
                            if (map.get("paramExclude") != null) {
                                map.put("paramExclude", map.get("paramExclude") + " " + descs);
                            } else {
                                map.put("paramExclude", descs);
                            }
                        } else if (explainSplit.startsWith("include")) {
                            String ss[] = explainSplit.split("include");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                String[] returnStr = ss[1].split("\\**/");
                                if (returnStr != null && returnStr.length > 0) {
                                    descs = returnStr[0];
                                }
                            } else {
                                descs = "";
                            }
                            if (map.get("paramInclude") != null) {
                                map.put("paramInclude", map.get("paramInclude") + " " + descs);
                            } else {
                                map.put("paramInclude", descs);
                            }
                        } else if (explainSplit.startsWith("return")) {
                            String ss[] = explainSplit.split("return");
                            String descs = "";
                            if (ss != null && ss.length >= 2) {
                                String[] returnStr = ss[1].split("\\**/");
                                if (returnStr != null && returnStr.length > 0) {
                                    descs = returnStr[0];
                                }
                            } else {
                                descs = "";
                            }
                            map.put("return", descs);
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

    public void actName(String filePath, String entityPrePath, String[] names, String dir) {
        String filePathName = "";
        for (String s : names) {
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
                        actName(filePathName, entityPrePath, fileDirNames, s);
                        break;
                    }
                    continue;
                }
                continue;
            }

            //直接是文件
            String[] strings = s.split(".java");
            Class<?> demo1 = null;
            List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> getAnnoInfo = anno(filePath, strings[0]);//解析注释
            try {
                if (dir != null) {
//                    demo1 = Class.forName("com.dounine.japi.web."+dir+"."+ strings[0]);
                    demo1 = Class.forName(entityPrePath + "." + dir + "." + strings[0]);
                } else {
//                    demo1 = Class.forName("com.dounine.japi.web." + strings[0]);
                    demo1 = Class.forName(entityPrePath + "." + strings[0]);
                }
                Method[] dan = demo1.getMethods();//获取所有方法
                for (Method method : dan) {
                    String methodName = method.getName();
                    if ("wait".equals(methodName)) {
                        continue;
                    } else if ("equals".equals(methodName)) {
                        continue;
                    } else if ("toString".equals(methodName)) {
                        continue;
                    } else if ("hashCode".equals(methodName)) {
                        continue;
                    } else if ("getClass".equals(methodName)) {
                        continue;
                    } else if ("notify".equals(methodName)) {
                        continue;
                    } else if ("notifyAll".equals(methodName)) {
                        continue;
                    }

                    Type[] type = method.getGenericParameterTypes();
                    List<Object> parmType = new ArrayList<Object>(1);
                    Map<String, Object> mapMethodParams = new HashMap<String, Object>(2);
                    for (Type ty : type) {
                        parmType.add(ty);

                        ClassPool pool = ClassPool.getDefault();
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
                                paramsValueStr.add(parameterTypes[i] + " " + attr.variableName(i + pos));
                            }
                            mapMethodParams.put("paramsValue", paramsValueStr);
                        }

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
                    }
                    mapMethodParams.put("class", s);
                    mapMethodParams.put("methodName", method.getName());
                    mapMethodParams.put("returnType", method.getReturnType().getName());
                    mapMethodParams.put("paramType", parmType);
                    Annotation[] actAnnos = demo1.getDeclaredAnnotations();
                    String[] rmVlues = null;
                    for (Annotation annotation : actAnnos) {
                        String annoSimpleName = annotation.annotationType().getSimpleName();
                        if ("RequestMapping".equals(annoSimpleName)) {
                            rmVlues = demo1.getDeclaredAnnotation(RequestMapping.class).value();
                        }
                    }
                    List<Map<String, Object>> annoList = new ArrayList<Map<String, Object>>();
                    Annotation[] actMethodAnnos = method.getDeclaredAnnotations();
                    for (Annotation annotation : actMethodAnnos) {
                        String[] methodValues = null;
                        RequestMethod[] reqMethods = null;
                        String[] reqParams = null;
                        String annoSimpleName = annotation.annotationType().getSimpleName();
                        if ("RequestMapping".equals(annoSimpleName)) {
//                            demoUrl(method,annoSimpleName,rmVlues,mapMethodParams);                            methodValues = method.getDeclaredAnnotation(PostMapping.class).value();

                            methodValues = method.getDeclaredAnnotation(RequestMapping.class).value();
                            reqMethods = method.getDeclaredAnnotation(RequestMapping.class).method();
                            reqParams = method.getDeclaredAnnotation(RequestMapping.class).params();
                            List<String> annoMap = new ArrayList<>();
                            if (methodValues != null) {
                                for (int i = 0; i < methodValues.length; i++) {
                                    String urlStr = "http://localhost:8080";
                                    if (reqMethods != null && reqMethods.length > 0) {
                                        urlStr = reqMethods[0] + " -> " + urlStr;
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
                        } else if ("PostMapping".equals(annoSimpleName)) {
                            demoUrl(method,annoSimpleName,rmVlues,mapMethodParams);                            methodValues = method.getDeclaredAnnotation(PostMapping.class).value();
//                            reqParams = method.getDeclaredAnnotation(PostMapping.class).params();
//                            List<String> annoMap = new ArrayList<>();
//                            if (methodValues != null) {
//                                for (int i = 0; i < methodValues.length; i++) {
//                                    String urlStr = "http://localhost:8080";
//                                    urlStr = "POST -> " + urlStr;
//                                    if (rmVlues != null && rmVlues.length > 0) {
//                                        urlStr = urlStr + "/" + rmVlues[0] + "/";
//                                    }
//                                    urlStr = urlStr + methodValues[i];
//                                    if (reqParams != null && reqParams.length > 0) {
//                                        urlStr = urlStr + " -> " + reqParams[i];
//                                    }
//                                    annoMap.add(urlStr);
//                                }
//                                mapMethodParams.put("demo", annoMap);
//                            }
                        } else if ("GetMapping".equals(annoSimpleName)) {
                            demoUrl(method,annoSimpleName,rmVlues,mapMethodParams);                            methodValues = method.getDeclaredAnnotation(GetMapping.class).value();
//                            reqParams = method.getDeclaredAnnotation(GetMapping.class).params();
//                            List<String> annoMap = new ArrayList<>();
//                            if (methodValues != null) {
//                                for (int i = 0; i < methodValues.length; i++) {
//                                    String urlStr = "http://localhost:8080";
//                                    urlStr = "GET -> " + urlStr;
//                                    if (rmVlues != null && rmVlues.length > 0) {
//                                        urlStr = urlStr + "/" + rmVlues[0] + "/";
//                                    }
//                                    urlStr = urlStr + methodValues[i];
//                                    if (reqParams != null && reqParams.length > 0) {
//                                        urlStr = urlStr + " -> " + reqParams[i];
//                                    }
//                                    annoMap.add(urlStr);
//                                }
//                                mapMethodParams.put("demo", annoMap);
//                            }
                        } else if ("PutMapping".equals(annoSimpleName)) {
                            demoUrl(method,annoSimpleName,rmVlues,mapMethodParams);                            methodValues = method.getDeclaredAnnotation(PutMapping.class).value();
//                            reqParams = method.getDeclaredAnnotation(PutMapping.class).params();
//                            List<String> annoMap = new ArrayList<>();
//                            if (methodValues != null) {
//                                for (int i = 0; i < methodValues.length; i++) {
//                                    String urlStr = "http://localhost:8080";
//                                    urlStr = "PUT -> " + urlStr;
//                                    if (rmVlues != null && rmVlues.length > 0) {
//                                        urlStr = urlStr + "/" + rmVlues[0] + "/";
//                                    }
//                                    urlStr = urlStr + methodValues[i];
//                                    if (reqParams != null && reqParams.length > 0) {
//                                        urlStr = urlStr + " -> " + reqParams[i];
//                                    }
//                                    annoMap.add(urlStr);
//                                }
//                                mapMethodParams.put("demo", annoMap);
//                            }
                        } else if ("PatchMapping".equals(annoSimpleName)) {
                            demoUrl(method,annoSimpleName,rmVlues,mapMethodParams);
//                            methodValues = method.getDeclaredAnnotation(PatchMapping.class).value();
//                            reqParams = method.getDeclaredAnnotation(PatchMapping.class).params();
//                            List<String> annoMap = new ArrayList<>();
//                            if (methodValues != null) {
//                                for (int i = 0; i < methodValues.length; i++) {
//                                    String urlStr = "http://localhost:8080";
//                                    urlStr = "PATCH -> " + urlStr;
//                                    if (rmVlues != null && rmVlues.length > 0) {
//                                        urlStr = urlStr + "/" + rmVlues[0] + "/";
//                                    }
//                                    urlStr = urlStr + methodValues[i];
//                                    if (reqParams != null && reqParams.length > 0) {
//                                        urlStr = urlStr + " -> " + reqParams[i];
//                                    }
//                                    annoMap.add(urlStr);
//                                }
//                                mapMethodParams.put("demo", annoMap);
//                            }
                        } else if ("DeleteMapping".equals(annoSimpleName)) {
                            demoUrl(method,annoSimpleName,rmVlues,mapMethodParams);

//                            methodValues = method.getDeclaredAnnotation(DeleteMapping.class).value();
//                            reqParams = method.getDeclaredAnnotation(DeleteMapping.class).params();
//                            List<String> annoMap = new ArrayList<>();
//                            if (methodValues != null) {
//                                for (int i = 0; i < methodValues.length; i++) {
//                                    String urlStr = "http://localhost:8080";
//                                    urlStr = "DELETE -> " + urlStr;
//                                    if (rmVlues != null && rmVlues.length > 0) {
//                                        urlStr = urlStr + "/" + rmVlues[0] + "/";
//                                    }
//                                    urlStr = urlStr + methodValues[i];
//                                    if (reqParams != null && reqParams.length > 0) {
//                                        urlStr = urlStr + " -> " + reqParams[i];
//                                    }
//                                    annoMap.add(urlStr);
//                                }
//                                mapMethodParams.put("demo", annoMap);
//                            }
                        }
                    }


                    for (int i = 0; i < getAnnoInfo.size(); i++) {
                        if (method.getName().equals(getAnnoInfo.get(i).get("methodName"))) {
                            mapMethodParams.put("methodName", getAnnoInfo.get(i).get("methodName"));
                            mapMethodParams.put("paramExclude", getAnnoInfo.get(i).get("paramExclude"));
                            mapMethodParams.put("paramInclude", getAnnoInfo.get(i).get("paramInclude"));
                            mapMethodParams.put("return", getAnnoInfo.get(i).get("return"));
                            mapMethodParams.put("desc", getAnnoInfo.get(i).get("desc"));
                        }

                    }
                    if(mapMethodParams.get("paramsValue") !=null){
                        if (mapMethodParams.get("paramInclude") != null) {

                            String paramInclude = mapMethodParams.get("paramInclude").toString().trim();
                            String[] paramIncludeArrs = paramInclude.split(" ");
                            String paramsValue = mapMethodParams.get("paramsValue").toString().trim();
                            String[] paramsValueArrs = paramsValue.split(",");
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
                                        paramTypeList.add(paramsValue);
                                        break;
                                    }
                                    if (!temp.equals(paramIncludeArrs[j])) {
                                        flag = false;
//                                            break;
                                    } else {
                                        flag = true;
                                    }
                                    if (flag) {
                                        paramTypeList.add(temps);
                                    }
                                }


                            }
                            Object paramTypeListToObject = paramTypeList;
                            mapMethodParams.put("paramTypeList", paramTypeListToObject);
                        }
                        if ((mapMethodParams.get("paramInclude") == null || mapMethodParams.get("paramInclude").equals("")) && mapMethodParams.get("paramExclude") != null) {
                            String paramInclude = mapMethodParams.get("paramExclude").toString().trim();
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
                                        break;
                                    }
                                    if (!temp.equals(paramIncludeArrs[j])) {
                                        flag = false;
//                                            break;
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
                        } else {
                            if (mapMethodParams.get("paramsValue") != null) {
                                String paramsValue = mapMethodParams.get("paramsValue").toString().trim();
                                String[] paramsValueAs1 = paramsValue.split("\\[");
                                String[] paramsValueAs2 = paramsValueAs1[1].split("\\]");
                                mapMethodParams.put("paramTypeList", paramsValueAs2);
                            }

                        }
                    }
//                    mapMethodParams.remove("paramType");
//                    mapMethodParams.remove("paramsValue");
                    classList.add(mapMethodParams);
                }
                System.out.println("秒:" + classList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String demoUrl(Method method,String reqMethod,String[] rmVlues,Map<String, Object> mapMethodParams){
        String[] methodValues = null;
        RequestMethod[] reqMethods = null;
        String[] reqParams = null;

        try {
            Class cls = Class.forName(reqMethod);
            System.out.println("泪妆换:"+cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(reqMethod.equals("PostMapping")){

        }

        methodValues = method.getDeclaredAnnotation(DeleteMapping.class).value();
        reqParams = method.getDeclaredAnnotation(DeleteMapping.class).params();
        List<String> annoMap = new ArrayList<>();
        if (methodValues != null) {
            for (int i = 0; i < methodValues.length; i++) {
                String urlStr = "http://localhost:8080";
                urlStr = "DELETE -> " + urlStr;
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
        return "";
    }
}
