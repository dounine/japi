package com.dounine.japi.web;

import com.dounine.japi.core.JapiServer;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.transfer.JapiNavRoot;
import com.dounine.japi.transfer.JapiProject;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private JapiServer japiServer = new JapiServer();

    @PostMapping("md5")
    public Result md5(String type,TransferInfo transferInfo) throws JapiException {
        ResultImpl result = new ResultImpl();
        if (StringUtils.isBlank(type)) {
            throw new JapiException("type not empty[logo,action,project].");
        }
        String md5 = null;
        switch (type) {
            case "logo":
                md5 = japiServer.getLogoMd5(transferInfo.getProjectName());
                break;
            case "action":
                md5 = japiServer.getActionMd5(transferInfo);
                break;
            case "project":
                md5 = japiServer.getProjectMd5(transferInfo.getProjectName());
                break;
            default:
                throw new JapiException(type + " not find.");
        }
        result.setData(md5);
        return result;
    }

    @PostMapping("navs")
    public Result navs(String projectName) throws JapiException {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        ResultImpl<JapiNavRoot> rest = new ResultImpl<JapiNavRoot>();
        rest.setData(japiServer.getProjectNav(projectName));
        return rest;
    }

    @GetMapping("{projectName}/logo")
    public void logo(HttpServletResponse response, @PathVariable String projectName) throws JapiException {
        response.setHeader("Content-Type", "image/png");
        InputStream fis = japiServer.getIconInputStream(projectName);//new FileInputStream("/home/lake/github/japi/html/img/logo.png");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = fis.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
            fis.close();
        } catch (IOException e) {
            throw new JapiException(e.getMessage());
        }

    }

    @PostMapping("versions")
    public Result versions(String projectName, String packageName, String funName, String actionName) throws JapiException {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("packageName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("funName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("actionName" + NOT_EMPTY_TIP);
        }
        ResultImpl rest = new ResultImpl();
        rest.setData(japiServer.getActionVersions(projectName, packageName, funName, actionName));
        return rest;
    }

    @PostMapping("dates")
    public Result dates(String projectName, String packageName, String funName, String actionName, String versionName) throws JapiException {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("packageName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("funName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("actionName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(versionName)) {
            throw new JapiException("versionName" + NOT_EMPTY_TIP);
        }
        ResultImpl rest = new ResultImpl();
        rest.setData(japiServer.getActionVerDates(projectName, packageName, funName, actionName, versionName));
        return rest;
    }

    @PostMapping("action")
    public void action(HttpServletResponse response, String projectName, String packageName, String funName, String actionName, String versionName, String dateName) throws JapiException {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(packageName)) {
            throw new JapiException("packageName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(funName)) {
            throw new JapiException("funName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(actionName)) {
            throw new JapiException("actionName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(versionName)) {
            throw new JapiException("versionName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(dateName)) {
            throw new JapiException("dateName" + NOT_EMPTY_TIP);
        }
        try {
            response.getWriter().print("{\"code\":0,\"msg\":null,\"data\":" + japiServer.getAction(projectName, packageName, funName, actionName, versionName, dateName) + "}");
        } catch (IOException e) {
            throw new JapiException(e.getMessage());
        }
    }

    @GetMapping("size")
    public Result size() throws JapiException {
        List<JapiProject> projects = japiServer.getAllProjects();
        ResultImpl rest = new ResultImpl();
        rest.setData(projects.size());
        return rest;
    }

    @GetMapping("lists/{page}/{size}")
    public Result lists(@PathVariable Integer page, @PathVariable Integer size) throws JapiException {
        if (page == 0) {
            page = 1;
        }
        if (size == 0) {
            size = 6;
        }
        List<JapiProject> projects = japiServer.getAllProjects();

        ResultImpl rest = new ResultImpl();
        int beginIndex = size * (page - 1);
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

    @PostMapping("exists")
    public Result exists(String projectName) throws JapiException {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        List<JapiProject> projects = japiServer.getAllProjects();
        ResultImpl<Boolean> result = new ResultImpl<>();
        result.setData(projects.stream().filter(p -> p.getName().equals(projectName)).findAny().isPresent());
        return result;
    }

}
