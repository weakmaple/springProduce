package org.litespring.aop;

/**
 * @objective : 结构化表达式的抽象接口
 * @date :2019/12/24- 8:51
 */
public interface Pointcut {
    MethodMatcher getMethodMatcher();
    String getExpression();
}
