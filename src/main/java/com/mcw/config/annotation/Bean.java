package com.mcw.config.annotation;

import java.lang.annotation.*;

/**
 * 用来得到方法返回的对象（spring配置类的Bean方法）
 */
@Target(ElementType.METHOD)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    String value() default "";
}
