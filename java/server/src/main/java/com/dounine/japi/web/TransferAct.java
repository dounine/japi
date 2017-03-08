package com.dounine.japi.web;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.core.JapiServer;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.transfer.JapiNavRoot;
import com.dounine.japi.transfer.JapiProject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by lake on 17-2-24.
 */
@RestController
@RequestMapping("transfer")
public class TransferAct {

    private JapiServer japiServer = new JapiServer();

    @PostMapping("project")
    public Result createProject(TransferInfo transferInfo) throws JapiException {
        japiServer.createProjectFold(transferInfo);
        return new ResultImpl<Boolean>(null, Boolean.TRUE);
    }

    @PostMapping("navs")
    public Result navs(TransferInfo transferInfo, String data) throws JapiException {
        JapiNavRoot japiNavRoot = JSON.parseObject(data, JapiNavRoot.class);
        japiServer.createNavs(transferInfo, japiNavRoot);
        return new ResultImpl<Boolean>(null, Boolean.TRUE);
    }

    @PostMapping("project/info")
    public Result createProject(TransferInfo transferInfo, @RequestParam("file") MultipartFile file) throws JapiException {
        if (!file.isEmpty()) {
            try {
                japiServer.saveProjectInfo(transferInfo, file.getOriginalFilename(), file.getInputStream());
                return new ResultImpl<String>(null, "success");
            } catch (Exception e) {
                ResultImpl<String> result = new ResultImpl("传输错误 " + e.getMessage());
                result.setCode(1);
                return result;
            }

        } else {
            ResultImpl<String> result = new ResultImpl("上传文件不能为空");
            result.setCode(1);
            return result;
        }
    }

    @PostMapping("action/info")
    public Result createActionInfo(TransferInfo transferInfo, @RequestParam("file") MultipartFile file) throws JapiException {
        if (!file.isEmpty()) {
            try {
                japiServer.saveActionInfo(transferInfo, file.getOriginalFilename(), file.getInputStream());
                return new ResultImpl<String>(null, "success");
            } catch (Exception e) {
                ResultImpl<String> result = new ResultImpl("传输错误 " + e.getMessage());
                result.setCode(1);
                return result;
            }
        } else {
            ResultImpl<String> result = new ResultImpl("上传文件不能为空");
            result.setCode(1);
            return result;
        }
    }

    @PostMapping("project/md5")
    public Result md5(String type, TransferInfo transferInfo) throws JapiException {
        ResultImpl result = new ResultImpl();
        if (StringUtils.isBlank(type)) {
            throw new JapiException("type not empty[logo,action,project].");
        }
        String md5 = null;
        switch (type) {
            case "logo":
                md5 = japiServer.getLogoMd5(transferInfo);
                break;
            case "action":
                md5 = japiServer.getActionMd5(transferInfo);
                break;
            case "project":
                md5 = japiServer.getProjectMd5(transferInfo);
                break;
            default:
                throw new JapiException(type + " not find.");
        }
        result.setData(md5);
        return result;
    }

    @PostMapping("project/exists")
    public Result exists(String projectName) throws JapiException {
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName 不能为空.");
        }
        List<JapiProject> projects = japiServer.getAllProjects();
        ResultImpl<Boolean> result = new ResultImpl<>();
        result.setData(projects.stream().filter(p -> p.getName().equals(projectName)).findAny().isPresent());
        return result;
    }

}
