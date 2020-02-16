package org.litespring.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @objective : 用于存储切面的方法
 * @date :2019/12/25- 9:02
 */
public interface Advice extends MethodInterceptor {
    public Pointcut getPointcut();
}
