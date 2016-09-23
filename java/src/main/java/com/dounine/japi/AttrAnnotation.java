package com.dounine.japi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ike on 16-9-13.
 */
@Target({ ElementType.FIELD })
// 标注只能放在类或接口的注解
@Retention(RetentionPolicy.RUNTIME)
//只在运行时起作用
public @interface AttrAnnotation {
    public String Name() default "名字";
    public String Description() default "描述";
    public String Constraints() default "约束";
    public String Requires() default "要求";
    public int MaxNum() default 48;
    public int MinNum() default 1;
    public boolean IsNUll() default true;//默认为空

}
