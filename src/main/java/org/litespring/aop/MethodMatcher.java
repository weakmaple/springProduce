package org.litespring.aop;

import java.lang.reflect.Method;

/**
 * @objective : 存储表达式的结构
 * @date :2019/12/24- 8:50
 */
public interface MethodMatcher {
    boolean matches(Method method);
}
