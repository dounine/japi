package com.dounine.japi.entity;

import com.dounine.japi.entity.u.UserChild;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

/**
 * Created by huanghuanlai on 2017/1/18.
 */
public class User {

    /**
     * 用户名
     * @reg 这是正则表达式
     * @des 这是描述信息
     */
    @NotBlank(message = "用户用户名不能为空",groups = {AddInterface.class})
    String username;
    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
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
