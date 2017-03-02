package com.dounine.japi.core.action.comm;

import com.dounine.japi.common.springmvc.ApiVersion;
import com.dounine.japi.entity.AddInterface;
import com.dounine.japi.entity.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻工具
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("{version}/article")
public class TestAction {

    /**
     * 获取热闹新闻
     * @param user 用户信息
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "list")
    @ApiVersion(1)
    @ResponseBody
    public User testUser(@Validated(value = {AddInterface.class}) User user,User mm, BindingResult bindingResult) {

        return null;
    }


}