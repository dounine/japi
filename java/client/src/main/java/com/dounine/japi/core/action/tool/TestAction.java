package com.dounine.japi.core.action.tool;

import com.dounine.japi.entity.AddInterface;
import com.dounine.japi.entity.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 普通用户
 * Created by huanghuanlai on 2017/1/18.
 */
@RestController
@RequestMapping("user/common")
public class TestAction {

    /**
     * 获取用户列表
     *
     * @param user 用户信息
     * @param cc   测试参数
     * @version v1
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "v1/list")
    @ResponseBody
    public User testUser(@Validated(value = {AddInterface.class}) User user, @RequestParam String cc, BindingResult bindingResult) {

        return null;
    }


}