package com.dounine.japi.test.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("home")
public class HomeAction {

    /**
     * 访问首页
     * @return {"index":"首页","login":"登录"}
     */
    @GetMapping("index")
    public String index(){
        return "success";
    }

}
