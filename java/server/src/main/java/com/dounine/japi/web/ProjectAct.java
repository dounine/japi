package com.dounine.japi.web;

import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.core.JapiServer;
import com.dounine.japi.exception.JapiException;
import com.dounine.japi.transfer.JapiNavRoot;
import com.dounine.japi.transfer.JapiProject;
import com.dounine.japi.web.type.FollowEnum;
import com.dounine.japi.web.type.SortTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lake on 17-2-24.
 */
@RestController
@RequestMapping("project")
public class ProjectAct {
    private static final String NOT_EMPTY_TIP = " 不能为空.";

    private JapiServer japiServer = new JapiServer();
    private static final List<String> TIPS = new ArrayList<>();

    static {
        TIPS.add("支持http测试.");
        TIPS.add("支持json,txt文件等测试.");
        TIPS.add("支持x-www-form-urlencoded,raw,binary上传格式.");
        TIPS.add("支持rest地址,http://japi.dounine.com/test/{id} 这样的地址会自动替换id.");
        TIPS.add("由于浏览器跨域访问限制,为了更好的测试服务,请下载安装chrome扩展:<a href='https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?utm_source=chrome-app-launcher-info-dialog'>postman</a>进行接口调试.");
        TIPS.add("为了规范API请求,所有JSON请求返回的格式都为{code:0,msg:null,data:null},code为响应编码,0默认为正确,非0为错误,msg为错误消息,data为响应数据.");
        TIPS.add("为了便于观查,响应参数都是{code:0,msg:null,data:null}此格式,响应参数都将默认填充于data字段中.");
        TIPS.add("请使用接口的最新更新的时间版本为主,更新时间列表只是为了对比接口所作的更改.");
        TIPS.add("凡是POST,PUT,PATCH,DELETE请求，必需添加幂等请求rtoken.");
        TIPS.add("GET请求获取数据过滤字段:params key:_excludes(该参数为数组) value(过滤字段) 如过滤name,age属性 _excludes:name _excludes:age");
        TIPS.add("GET请求获取数据导入字段，(只查询_includes字段):params key:_includes(该参数为数组) value(过滤字段) 如只查询name,age属性 _includes:name _includes:age");

    }

    private String getToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Object tokenObject = httpServletRequest.getHeader("token");
        if (null == tokenObject) {
            tokenObject = httpServletRequest.getParameter("token");
        }
        if (null == tokenObject) {
            throw new JapiException("请求头token不能为空");
        }
        return tokenObject.toString();
    }

    @GetMapping("tip")
    public Result tip(){
        ResultImpl result = new ResultImpl();
        result.setData(TIPS);
        return result;
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

    @PostMapping("delFollow")
    public Result followDel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String projectName) throws JapiException {
        String token = getToken(httpServletRequest, httpServletResponse);

        ResultImpl result = new ResultImpl();
        if (StringUtils.isBlank(projectName)) {
            throw new JapiException("projectName" + NOT_EMPTY_TIP);
        }
        japiServer.follow(token, projectName, FollowEnum.DEL);
        result.setMsg("success");
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
        versions.sort((b, a) -> {
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

    @GetMapping("lists/{offset}/{limit}")
    public Result lists(@PathVariable Integer offset, @PathVariable Integer limit, String sortName, SortTypeEnum sortType, HttpServletRequest request, HttpServletResponse response) throws JapiException {

        if (offset <= 0) {
            offset = 1;
        }
        if (limit <= 0) {
            limit = 8;
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
        int beginIndex = limit * (offset - 1);
        if (beginIndex > projects.size()) {
            beginIndex = 0;
        }
        int endIndex = beginIndex + limit;
        if (endIndex > projects.size()) {
            endIndex = projects.size();
        }
        List<JapiProject> japiProjects = projects.subList(beginIndex, endIndex);
        String token = getToken(request, response);
        List<String> projectNames = japiServer.getFollows(token);
        for (String projectName : projectNames) {
            final String _projectName = projectName;
            japiProjects.forEach(jp -> {
                if (jp.getName().equals(_projectName)) {
                    jp.setFollow(true);
                }
            });
        }

        rest.setData(japiProjects);
        return rest;
    }



}
