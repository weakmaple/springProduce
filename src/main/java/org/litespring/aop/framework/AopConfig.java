package org.litespring.aop.framework;

import org.litespring.aop.Advice;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @objective : aop的配置接口，实现 存储切面的方法
 * @date :2019/12/26- 13:43
 */
public interface AopConfig {
    Class<?> getTargetClass();
    Object getTargetObject();
    boolean isProxyTargetClass();
    Class<?>[] getProxiedInterfaces();
    boolean isInterfaceProxied(Class<?> intf);
    List<Advice> getAdvices();
    void addAdvice(Advice advice);
    List<Advice> getAdvices(Method method);
    void setTargetObject(Object obj);
}
