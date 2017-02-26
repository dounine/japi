package com.dounine.japi.web;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.JapiServer;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.transfer.JapiNavRoot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by lake on 17-2-24.
 */
@RestController
@RequestMapping("transfer")
public class TransferAct {

    private JapiServer japiServer = new JapiServer();

    @PostMapping("project")
    public Result createProject(TransferInfo transferInfo) throws JapiException {
        japiServer.createProjectFold(transferInfo.getProjectName());
        return new ResultImpl<Boolean>(null, Boolean.TRUE);
    }

    @PostMapping("navs")
    public Result navs(TransferInfo transferInfo, String data) throws JapiException {
        JapiNavRoot japiNavRoot = JSON.parseObject(data, JapiNavRoot.class);
        japiServer.createNavs(transferInfo, japiNavRoot);
        return new ResultImpl<Boolean>(null, Boolean.TRUE);
    }

    @PostMapping("projectInfo")
    public Result createProject(TransferInfo transferInfo, @RequestParam("file") MultipartFile file) throws JapiException {
        if (!file.isEmpty()) {
            try {
                japiServer.saveProjectInfo(transferInfo.getProjectName(), file.getOriginalFilename(), file.getInputStream());
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

    @PostMapping("actionInfo")
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

}
