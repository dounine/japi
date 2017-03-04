package com.dounine.japi.entity;

import com.dounine.japi.entity.u.UserChild;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;

/**
 * 用户类信息
 */
@RequestMapping
public class User {

    /**
     * 用户名
     * @reg 这是正则表达式
     * @des 这是描述信息
     */
    @NotNull(message = "用户名不能为空",groups = {AddInterface.class})
    protected String username;
    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空",groups = {AddInterface.class})
    private String password;
    /**
     * 测试子类
     */
    private UserChild userChild;
    /**
     * 我是我
     */
    private User user;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserChild getUserChild() {
        return userChild;
    }

    public void setUserChild(UserChild userChild) {
        this.userChild = userChild;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
