package org.litespring.aop.framework;

/**
 * @objective :
 * @date :2019/12/26- 14:08
 */
public interface AopProxyFactory {
    Object getProxy();
    Object getProxy(ClassLoader classLoader);
}
