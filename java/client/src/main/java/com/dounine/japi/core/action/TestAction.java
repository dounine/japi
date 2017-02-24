package com.dounine.japi.core.action;

import com.dounine.japi.common.springmvc.ApiVersion;
import com.dounine.japi.entity.AddInterface;
import com.dounine.japi.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("{version}/asdf/main")
public class TestAction {

    /**
     * 测试例子
     * @param user 用户信息
     * @param cc   测试参数
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "aa")
    @ApiVersion(1)
    @ResponseBody
    public User testUser(@Validated(value = {AddInterface.class}) User user, @RequestParam String cc, BindingResult bindingResult) {

        return null;
    }

    /**
     * 测试例子
     * @param user 用户信息
     * @param cc   测试参数
     * @version 2
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "aa")
    @ApiVersion(2)
    @ResponseBody
    public User testUser2(@Validated(value = {AddInterface.class}) User user, @RequestParam String cc, BindingResult bindingResult) {
        return null;
    }


}