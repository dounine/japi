package com.dounine.japi.test.web.user;

import com.dounine.japi.test.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("user")
public class UserAction {

    /**
     * 登录
     * @param user 用户信息
     * @return {"success":"成功","error":"失败"}
     */
    @GetMapping("login")
    public String login(User user){
        return "success";
    }

}
