package com.mcw.config.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
}
