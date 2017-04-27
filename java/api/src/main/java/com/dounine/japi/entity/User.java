package com.dounine.japi.entity;

import com.dounine.japi.entity.u.UserChild;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户类信息
 */
public class User {
    /**
     * 用户名
     *
     * @des 优先级大于用户名
     * @req true
     * @def 这是默认值
     * @con 这是约束条件
     */
    protected String username;
    /**
     * 验证带正则字符串
     */
    @Pattern(groups = {User.UserADD.class}, regexp = "IKE\\d{6}", message = "员工编号为IKE开头,后面为6位数字")
    private String peopleNumber;
    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空", groups = {User.UserADD.class})
    private String password;
    /**
     * 测试子类
     */
    private UserChild userChild;
    /**
     * 测试enum类型
     */
    @NotNull(message = "enum type 不能为空")
    private TestType testType;
    /**
     * 我是我
     */
    private User user;
    /**
     * 测试users列表
     */
    private User[] userList;

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

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public User[] getUserList() {
        return userList;
    }

    public void setUserList(User[] userList) {
        this.userList = userList;
    }

    public String getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(String peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public interface UserADD {
    }

    public interface UserDEL {
    }

    public interface UserUPDATE {
    }

    public interface UserPUT {
    }
}
