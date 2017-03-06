package com.dounine.japi.core.action.comm;

import com.dounine.japi.act.Result;
import com.dounine.japi.act.ResultImpl;
import com.dounine.japi.common.springmvc.ApiVersion;
import com.dounine.japi.entity.AddInterface;
import com.dounine.japi.entity.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * @throws RuntimeException
     * @deprecated yes
     * @return class User
     */
    @ApiVersion(1)
    @org.springframework.web.bind.annotation.GetMapping(value = "hots")
    @ResponseBody
    public Result hots(@Validated User user, BindingResult bindingResult) throws RuntimeException {

        return null;
    }

    /**
     * 获取冷门新闻
     *
     * @param id 用户
     * @throws RuntimeException
     * @deprecated yes
     */
    @ApiVersion(1)
    @org.springframework.web.bind.annotation.GetMapping(value = "list/{id}")
    @ResponseBody
    public Result testUser(@PathVariable String id) throws RuntimeException {

        return null;
    }


}