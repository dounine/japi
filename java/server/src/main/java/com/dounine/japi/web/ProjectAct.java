package com.dounine.japi.web;

import com.dounine.japi.core.JapiServer;
import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.transfer.JapiNavRoot;
import com.dounine.japi.transfer.JapiProject;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    private String getToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Object tokenObject = httpServletRequest.getHeader("token");
        if (null == tokenObject) {
            tokenObject = httpServletRequest.getParameter("token");
        }
        if (null == tokenObject) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        tokenObject = cookie.getValue();
                        break;
                    }
                }
            }

        }
        if (null == tokenObject) {
            throw new JapiException("请求头token不能为空");
        }
        return tokenObject.toString();
    }

    @PostMapping("follow")
    public Result followAdd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String projectName) throws JapiException {
        String token = getToken(httpServletRequest, httpServletResponse);
        ResultImpl result = new ResultImpl();
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        japiServer.follow(token, projectName, FollowEnum.ADD);
        result.setMsg("success");
        return result;
    }

    @DeleteMapping("follow/{projectName}")
    public Result followDel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,@PathVariable String projectName) throws JapiException {
        String token = getToken(httpServletRequest, httpServletResponse);

        ResultImpl result = new ResultImpl();
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        japiServer.follow(token, projectName, FollowEnum.DEL);
        result.setMsg("success");
        return result;
    }


    @PostMapping("md5")
    public Result md5(String type, TransferInfo transferInfo) throws JapiException {
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
        InputStream fis = japiServer.getIconInputStream(projectName);
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
    public Result versions(TransferInfo transferInfo) throws JapiException {
        if (StringUtils.isBlank(transferInfo.getProjectName())) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getPackageName())) {
            throw new JapiException("packageName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getFunName())) {
            throw new JapiException("funName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getActionName())) {
            throw new JapiException("actionName" + NOT_EMPTY_TIP);
        }
        ResultImpl rest = new ResultImpl();
        List<String> versions = japiServer.getActionVersions(transferInfo);
        versions.sort((b,a)->{
            return new Integer(Integer.parseInt(a.substring(1))).compareTo(Integer.parseInt(b.substring(1)));
        });
        rest.setData(versions);
        return rest;
    }

    @PostMapping("dates")
    public Result dates(TransferInfo transferInfo) throws JapiException {
        if (StringUtils.isBlank(transferInfo.getProjectName())) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getPackageName())) {
            throw new JapiException("packageName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getFunName())) {
            throw new JapiException("funName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getActionName())) {
            throw new JapiException("actionName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getVersionName())) {
            throw new JapiException("versionName" + NOT_EMPTY_TIP);
        }
        ResultImpl rest = new ResultImpl();
        List<String> datas = japiServer.getActionVerDates(transferInfo);
        datas.sort((b, a) -> a.compareTo(b));
        rest.setData(datas);
        return rest;
    }

    @PostMapping("action")
    public void action(HttpServletResponse response, TransferInfo transferInfo) throws JapiException {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        if (StringUtils.isBlank(transferInfo.getProjectName())) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getPackageName())) {
            throw new JapiException("packageName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getFunName())) {
            throw new JapiException("funName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getActionName())) {
            throw new JapiException("actionName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getVersionName())) {
            throw new JapiException("versionName" + NOT_EMPTY_TIP);
        }
        if (StringUtils.isBlank(transferInfo.getDateName())) {
            throw new JapiException("dateName" + NOT_EMPTY_TIP);
        }
        try {
            response.getWriter().print("{\"code\":0,\"msg\":null,\"data\":" + japiServer.getAction(transferInfo) + "}");
        } catch (IOException e) {
            throw new JapiException(e.getMessage());
        }
    }

    @GetMapping("count")
    public Result count() throws JapiException {
        ResultImpl rest = new ResultImpl();
        rest.setData(japiServer.getAllProjects().size());
        return rest;
    }

    @GetMapping("lists/{page}/{size}")
    public Result lists(@PathVariable Integer page, @PathVariable Integer size, String sortName, SortTypeEnum sortType,HttpServletRequest request,HttpServletResponse response) throws JapiException {

        if (page == 0) {
            page = 1;
        }
        if (size == 0) {
            size = 6;
        }
        List<JapiProject> projects = japiServer.getAllProjects();
        if (StringUtils.isNotBlank(sortName)) {
            if ("createTime".equals(sortName)) {
                if (null == sortType || (null != sortType && sortType.equals(SortTypeEnum.ASC))) {
                    projects.sort((a, b) -> a.getCreateTime().compareTo(b.getCreateTime()));
                } else {
                    projects.sort((b, a) -> a.getCreateTime().compareTo(b.getCreateTime()));
                }
            } else if ("author".equals(sortName)) {
                if (null == sortType || (null != sortType && sortType.equals(SortTypeEnum.ASC))) {
                    projects.sort((a, b) -> a.getAuthor().compareTo(b.getAuthor()));
                } else {
                    projects.sort((b, a) -> a.getAuthor().compareTo(b.getAuthor()));
                }
            } else if ("name".equals(sortName)) {
                if (null == sortType || (null != sortType && sortType.equals(SortTypeEnum.ASC))) {
                    projects.sort((a, b) -> a.getName().compareTo(b.getName()));
                } else {
                    projects.sort((b, a) -> a.getName().compareTo(b.getName()));
                }
            }
        }

        ResultImpl rest = new ResultImpl();
        int beginIndex = size * (page - 1);
        if (beginIndex > projects.size()) {
            beginIndex = 0;
        }
        int endIndex = beginIndex + size;
        if (endIndex > projects.size()) {
            endIndex = projects.size();
        }
        List<JapiProject> japiProjects = projects.subList(beginIndex, endIndex);
        String token = getToken(request,response);
        List<String> projectNames = japiServer.getFollows(token);
        for(String projectName : projectNames){
            final String _projectName = projectName;
            japiProjects.forEach(jp->{
                if(jp.getName().equals(_projectName)){
                    jp.setFollow(true);
                }
            });
        }

        rest.setData(japiProjects);
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
