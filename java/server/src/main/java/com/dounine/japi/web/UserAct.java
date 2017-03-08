package com.dounine.japi.web;

import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.auth.UserAuth;
import com.dounine.japi.auth.UserUtils;
import com.dounine.japi.core.JapiServer;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/15.
 */
@RestController
@RequestMapping("user")
public class UserAct {

    private JapiServer japiServer = new JapiServer();

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

    @GetMapping("follows")
    public Result follows(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request, response);
        ResultImpl result = new ResultImpl();
        result.setData(japiServer.getFollows(token));
        return result;
    }

    @PostMapping("follows/sortAndDel")
    public Result follows(String[] projects, HttpServletRequest request, HttpServletResponse response) {
        if (null == projects) {
            throw new JapiException("projects 数组不能为空.");
        }
        String token = getToken(request, response);
        ResultImpl result = new ResultImpl();
        List<String> follows = japiServer.getFollows(token);
        if (follows == null || (follows != null && follows.size() != projects.length)) {
            throw new JapiException("数据只能用于排序,不能增加或减少");
        }
        int equalsCount = 0;
        for (String follow : follows) {
            for (String projectName : projects) {
                if (projectName.equals(follow)) {
                    equalsCount++;
                    break;
                }
            }
        }
        if (follows.size() != equalsCount) {
            throw new JapiException("数据只能用于排序,不能增加或减少");
        }
        result.setData(japiServer.sortAndDel(token, projects));
        return result;
    }

    @PostMapping("login")
    public Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) throws JapiException {
        if (StringUtils.isBlank(username)) {
            throw new JapiException("username 不能为空.");
        }
        if (StringUtils.isBlank(password)) {
            throw new JapiException("password 不能为空.");
        }
        String token = UserUtils.login(new UserAuth(username, password));
        if (null != token) {
            response.setHeader("token", token);
            return new ResultImpl("success", token);
        } else {
            throw new JapiException("用户名或密码错误.");
        }
    }

    @PostMapping("isLogin")
    public Result login(String token) throws JapiException {
        if (StringUtils.isBlank(token)) {
            throw new JapiException("token 不能为空.");
        }
        boolean isAuth = UserUtils.isAuth(token);
        return new ResultImpl("success", isAuth);
    }

    @GetMapping("logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) throws JapiException {
        String token = getToken(request, response);

        if (UserUtils.logout(token)) {
            return new ResultImpl("success", "/user/login");
        }
        return new ResultImpl("error", "请登录后再操作.");
    }


}
