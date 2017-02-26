package com.dounine.japi.web;

import com.dounine.japi.JapiServer;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by lake on 17-2-24.
 */
@RestController
@RequestMapping("transfer")
public class TransferAct {

    private JapiServer japiServer = new JapiServer();

    @PostMapping("project")
    public Result createProject(TransferInfo transferInfo){
        japiServer.createProjectFold(transferInfo.getProjectName());
        return new ResultImpl<Boolean>(null,true);
    }

    @PostMapping("projectInfo")
    public Result createProject(TransferInfo transferInfo,@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                japiServer.saveProjectInfo(transferInfo.getProjectName(),file.getOriginalFilename(),file.getInputStream());
                return new ResultImpl(null,"success");
            } catch (Exception e) {
                ResultImpl result = new ResultImpl("传输错误 " + e.getMessage());
                result.setCode(1);
                return result;
            }

        } else {
            ResultImpl result = new ResultImpl("上传文件不能为空");
            result.setCode(1);
            return result;
        }
    }

    @PostMapping("package")
    public Result createPackage(TransferInfo transferInfo){
        return null;
    }

    @PostMapping("fun")
    public Result createFun(TransferInfo transferInfo){
        return null;
    }

    @PostMapping("action")
    public Result createAction(TransferInfo transferInfo){
        return null;
    }

    @PostMapping("version")
    public Result createVersion(TransferInfo transferInfo){
        return null;
    }

    @PostMapping("dates")
    public Result createDates(TransferInfo transferInfo){
        return null;
    }

    @PostMapping("actionInfo")
    public Result createActionInfo(TransferInfo transferInfo,@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {

                return new ResultImpl(null,"xx");
            } catch (Exception e) {
                ResultImpl result = new ResultImpl("传输错误 " + e.getMessage());
                result.setCode(1);
                return result;
            }

        } else {
            ResultImpl result = new ResultImpl("上传文件不能为空");
            result.setCode(1);
            return result;
        }
    }

}
