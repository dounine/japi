package com.dounine.japi.core.action.bb;

import com.dounine.japi.entity.AddInterface;
import com.dounine.japi.entity.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 12323456
 * Created by lake on 17-2-23.
 */
public class AA {
    /**
     * 测试例子
     *
     * @param user 用户信息
     * @param cc   测试参数
     */
    @org.springframework.web.bind.annotation.GetMapping(value = "aa")
    @ResponseBody
    public User testUser(@Validated(value = {AddInterface.class}) User user, @RequestParam String cc, BindingResult bindingResult) {
        return null;
    }
}
