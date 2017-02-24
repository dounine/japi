package com.dounine.japi.web;

import com.dounine.japi.JapiServer;
import com.dounine.japi.entity.JapiProject;
import com.dounine.japi.exception.JapiException;
import jdk.internal.util.xml.impl.Input;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by lake on 17-2-24.
 */
@RestController
@RequestMapping("project")
public class ProjectAct {
    private static final String NOT_EMPTY_TIP = " 不能为空.";

    @PostMapping("nav")
    public Result findProject(String projectName) throws Exception {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName"+NOT_EMPTY_TIP);
        }
        RestImpl rest = new RestImpl();
        rest.setData(JapiServer.getProjectNav(projectName));
        return rest;
    }

    @GetMapping("{projectName}/logo")
    public void logo(HttpServletResponse response, @PathVariable String projectName) throws Exception {
        InputStream fis = JapiServer.getIconInputStream(projectName);//new FileInputStream("/home/lake/github/japi/html/img/logo.png");
        OutputStream os = response.getOutputStream();
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = fis.read(bytes))!=-1){
            os.write(bytes,0,len);
        }
        os.flush();
        fis.close();
    }

    @PostMapping("versions")
    public Result version(String projectName,String packageName,String funName,String actionName) throws Exception {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("packageName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("funName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("actionName"+NOT_EMPTY_TIP);
        }
        RestImpl rest = new RestImpl();
        rest.setData(JapiServer.getActionVersions(projectName,packageName,funName,actionName));
        return rest;
    }

    @PostMapping("dates")
    public Result dates(String projectName,String packageName,String funName,String actionName,String versionName) throws Exception {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("packageName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("funName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("actionName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(versionName)) {
            throw new JapiException("versionName"+NOT_EMPTY_TIP);
        }
        RestImpl rest = new RestImpl();
        rest.setData(JapiServer.getActionVerDates(projectName,packageName,funName,actionName,versionName));
        return rest;
    }

    @PostMapping("action")
    public Result action(String projectName,String packageName,String funName,String actionName,String versionName,String dateName) throws Exception {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("packageName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("funName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("actionName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(versionName)) {
            throw new JapiException("versionName"+NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(dateName)) {
            throw new JapiException("dateName"+NOT_EMPTY_TIP);
        }
        RestImpl rest = new RestImpl();
        rest.setData(JapiServer.getAction(projectName,packageName,funName,actionName,versionName,dateName));
        return rest;
    }

    @GetMapping("size")
    public Result size() {
        List<JapiProject> projects = JapiServer.getAllProjects();
        RestImpl rest = new RestImpl();
        rest.setData(projects.size());
        return rest;
    }

    @GetMapping("list/{page}/{size}")
    public Result findProject(@PathVariable Integer page, @PathVariable Integer size) {
        if (page == 0) {
            page = 1;
        }
        if (size == 0) {
            size = 6;
        }
        List<JapiProject> projects = JapiServer.getAllProjects();

        RestImpl rest = new RestImpl();
        int beginIndex = size * (page-1);
        if (beginIndex > projects.size()) {
            beginIndex = 0;
        }
        int endIndex = beginIndex + size;
        if (endIndex > projects.size()) {
            endIndex = projects.size();
        }
        rest.setData(projects.subList(beginIndex, endIndex));
        return rest;
    }

}
