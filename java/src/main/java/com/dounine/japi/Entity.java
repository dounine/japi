package com.dounine.japi;

/**
 * Created by ike on 16-9-13.
 */
public class Entity {
    @AttrAnnotation(Name="name",Description = "姓名",IsNUll = false)
    private String name;
    @AttrAnnotation(Name = "age",Description = "年龄",Constraints = "年龄值要大于0",IsNUll = false)
    private Long age;
    @AttrAnnotation(Name = "sex",Description = "性别(男:MAN;女:FIMAIL)")
    private Sex sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}
