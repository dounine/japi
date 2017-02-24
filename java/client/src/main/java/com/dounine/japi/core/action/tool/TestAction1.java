package com.dounine.japi.core.action.tool;

import com.dounine.japi.common.springmvc.ApiVersion;
import com.dounine.japi.entity.AddInterface;
import com.dounine.japi.entity.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * VIP用户
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("{version}/user/tool")
public class TestAction1 {

    /**
     * 获取用户列表
     *
     * @param user 用户信息
     * @param cc 测试参数
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "list")
    @ApiVersion(1)
    @ResponseBody
    public User list(@Validated(value = {AddInterface.class}) User user, @RequestParam String cc, BindingResult bindingResult) {

        return null;
    }

    /**
     * 删除用户
     *
     * @param user 用户信息
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "del")
    @ApiVersion(1)
    @ResponseBody
    public User del(@Validated(value = {AddInterface.class}) User user, BindingResult bindingResult) {

        return null;
    }

    /**
     * 新增用户
     *
     * @param user 用户信息
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "add")
    @ApiVersion(1)
    @ResponseBody
    public User add(@Validated(value = {AddInterface.class}) User user, BindingResult bindingResult) {

        return null;
    }


}