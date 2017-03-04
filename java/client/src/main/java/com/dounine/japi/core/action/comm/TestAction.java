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
     * @deprecated yes
     * @throws RuntimeException
     */
    @ApiVersion(1)
    @org.springframework.web.bind.annotation.GetMapping(value = "list")
    @ResponseBody
    public Result testUser(@Validated User user, String[] names, BindingResult bindingResult) throws RuntimeException{

        return null;
    }

    /**
     * 获取冷门新闻
     * @deprecated yes
     * @param id 用户
     * @throws RuntimeException
     */
    @ApiVersion(1)
    @org.springframework.web.bind.annotation.GetMapping(value = "list/{id}")
    @ResponseBody
    public Result testUser(@PathVariable String id) throws RuntimeException{

        return null;
    }


}