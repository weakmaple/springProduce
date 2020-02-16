package org.litespring.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @objective : 设计Autowired注解
 * @date :2019/11/22- 7:36
 */
@Target({ElementType.CONSTRUCTOR,ElementType.FIELD,ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
