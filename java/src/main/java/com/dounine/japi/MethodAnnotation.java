package com.dounine.japi;

import java.lang.annotation.*;
import java.util.List;

/**
 * Created by ike on 16-9-13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface MethodAnnotation {
    public String name() default "方法名";
    public String returnType ()default "返回类型";
    public String[] param() default "";
    public String[] paramType() default "";

}
