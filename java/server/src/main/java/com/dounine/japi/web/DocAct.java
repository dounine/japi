package com.dounine.japi.web;

import com.dounine.japi.Task;
import com.dounine.japi.annotation.MethodVersion;
import com.dounine.japi.format.JSPFormat;
import com.dounine.japi.utils.JspFileDealUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ike on 16-10-28.
 */


@RestController
@RequestMapping("interfaceapidoc")
public class DocAct {

    @Autowired
    private Render render;
    @Value("${htmlRootPath}")
    private String htmlRootPath;


    @GetMapping("index/{module}")
    public void index(@PathVariable String module, HttpServletResponse response){
        render.show(module+"/__index");
    }

    @GetMapping("tpls/tpl_guide/{a}/{b}/{c}.html")
    public void tpl(@PathVariable String a,@PathVariable String b,@PathVariable String c,HttpServletResponse response){
        String filePath = a+"/"+b.replace("-","/")+"/"+c;
        render.show(filePath);
    }

    @PostMapping("upload")
    public String receiveFile(@RequestParam String projectName,@RequestParam(name = "filePathParameter",defaultValue = "") String filePath,@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            String projectDir = htmlRootPath+"/"+projectName;//*/WEB-INF/views/interfaceapidoc/demo-card-consumer
            File projectFold = new File(projectDir);
            if(!projectFold.exists()){
                projectFold.mkdir();
            }
            File childFold = new File(projectDir+"/"+filePath);
            if(!childFold.exists()){
                childFold.mkdirs();
            }
            String fileAbsolutePath = projectDir+"/"+filePath+file.getOriginalFilename();
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileAbsolutePath)));
                stream.write(bytes);
                stream.flush();
                stream.close();
                replaceIndex(file.getOriginalFilename(),projectName);
                return "传输完成";
            } catch (Exception e) {
                return "传输错误 " + e.getMessage();
            }

        } else {
            return "上传文件不能为空";
        }
    }

    public void replaceIndex(String guideName,String projectName) throws IOException {
        String indexJspContent = FileUtils.readFileToString(new File(htmlRootPath + "/index.html"));
        String Jspcontents = Task.indexJspDeal(indexJspContent,guideName,projectName);//"__index.html"
        try {
            FileUtils.writeStringToFile(new File(htmlRootPath + "/index.html"),Jspcontents,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("uploads")
    public String receiveFile(String projectName,@RequestParam("files") MultipartFile[] files){
        if (null!=files&&files.length>0) {
            String projectDir = htmlRootPath+"/"+projectName;//*/WEB-INF/views/interfaceapidoc/demo-card-consumer
            File projectFold = new File(projectDir);
            if(!projectFold.exists()){
                projectFold.mkdir();
            }

            for(MultipartFile multipartFile : files){

                try {
                    byte[] bytes = multipartFile.getBytes();
                    BufferedOutputStream stream =
                            new BufferedOutputStream(new FileOutputStream(new File(multipartFile.getName())));
                    stream.write(bytes);
                    stream.close();

                } catch (Exception e) {
                    return "传输错误 " + e.getMessage();
                }
            }
            return "传输完成";
        } else {
            return "上传文件不能为空";
        }
    }
}

