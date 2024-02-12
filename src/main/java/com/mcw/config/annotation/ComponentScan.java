package com.mcw.config.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ComponentScan {
    String[] basePackages() default {};
}
