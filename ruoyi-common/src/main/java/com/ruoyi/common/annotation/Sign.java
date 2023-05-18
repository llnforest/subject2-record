package com.ruoyi.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sign {
    String key() default "abcdeft";
    boolean verifyTime() default false;
}
