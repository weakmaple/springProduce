package org.litespring.stereotype;

import java.lang.annotation.*;

/**
 * @objective :
 * @date :2019/11/22- 7:33
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
