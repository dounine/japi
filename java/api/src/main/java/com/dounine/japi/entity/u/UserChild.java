package com.dounine.japi.entity.u;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 子用户的信息
 * Created by huanghuanlai on 2017/1/18.
 */
public class UserChild {

    /**
     * 爱人
     */
    @NotBlank
    String love;
    /**
     * 年龄
     */
    @Min(value = 10)
    private Integer age;
    /**
     * 年龄jj
     */
    private JJ jj;

    public JJ getJj() {
        return jj;
    }

    public void setJj(JJ jj) {
        this.jj = jj;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
